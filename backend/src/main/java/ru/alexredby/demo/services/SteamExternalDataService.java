package ru.alexredby.demo.services;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamPlayerService;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamStorefront;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexredby.demo.enums.UserVisibility;
import ru.alexredby.demo.exceptions.UserNotFoundException;
import ru.alexredby.demo.persistance.models.Application;
import ru.alexredby.demo.persistance.models.User;
import ru.alexredby.demo.persistance.services.ApplicationDataService;
import ru.alexredby.demo.persistance.services.UserDataService;
import ru.alexredby.demo.utils.SteamUtils;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

/**
 * Service connecting a Steam Api with inner DB.
 */
@Service
@Transactional
public class SteamExternalDataService {

    private SteamUser steamUser;
    private SteamPlayerService steamPlayerService;
    private SteamStorefront steamStorefront;

    private UserDataService userDataService;
    private ApplicationDataService applicationDataService;

    @Autowired
    public SteamExternalDataService(
            UserDataService userDataService,
            ApplicationDataService applicationDataService,
            SteamWebApiClient steamWebApiClient
    ) {
        this.userDataService = userDataService;
        this.applicationDataService = applicationDataService;
        this.steamUser = new SteamUser(steamWebApiClient);
        this.steamPlayerService = new SteamPlayerService(steamWebApiClient);
        this.steamStorefront = new SteamStorefront(steamWebApiClient);
    }

    /**
     * Updates or creates user and all related user information.
     *
     * @param userId a steam id of user to update or create.
     * @return an user which was updated or created.
     */
    public User updateOrCreateUser(@NotNull Long userId) throws UserNotFoundException, CompletionException {
        // Start request to Steam Api through CompletableFuture
        final var profileFuture = steamUser.getPlayerProfile(userId);
        User user = userDataService.findById(userId).orElse(new User(userId));

        // Waiting answer from Steam Api
        @Nullable
        final SteamPlayerProfile profile = profileFuture.join();

        // Check if id was wrong
        if (profile == null) {
            throw new UserNotFoundException("User wasn't found through Steam Api. Check id if it correct");
        }

        user.update(profile);

        updateGamesOf(user);
        // Что делать если пришло, например, 5000 игр? проверять их наличие в БД или нет?

        // Change lastUpdate before save in DB
        user.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

        user = userDataService.save(user);

        return user;
    }

    /**
     *
     *
     * @param user whose games need to be updated
     */
    private void updateGamesOf(@NotNull User user) {
        // Check if profile is public. If it is - update, otherwise do nothing
        if(user.getCommunityVisibilityState() == UserVisibility.PUBLIC) {
            CompletableFuture<List<SteamPlayerOwnedGame>> gamesFuture = null;
            // Check if 2 weeks passed after last update
            if (ChronoUnit.WEEKS.between(user.getLastUpdate(), LocalDateTime.now()) >= 2) {
                gamesFuture = steamPlayerService.getOwnedGames(user.getId(), true, true);
            } else {
                // Count = 0 means all games
                gamesFuture = steamPlayerService.getRecentlyPlayedGames(user.getId(), 0).thenApply(list ->
                        list.stream()
                                .map(SteamUtils::convertRecentGamesToOwned)
                                .collect(Collectors.toList())
                );
            }
            List<SteamPlayerOwnedGame> games = gamesFuture.join();

            List<Application> existingApplications = applicationDataService.findAllById(
                    games.stream()
                            .map(SteamPlayerOwnedGame::getAppId)
                            .collect(Collectors.toList())
            );

            // Check if this games exists in DB
            List<Application> applications = games.stream()
                    .filter(g -> existingApplications.stream().noneMatch(app -> app.getId() == g.getAppId()))
                    .map(g -> {
                        @Nullable
                        StoreAppDetails appDetails = steamStorefront.getAppDetails(g.getAppId()).join();
                        return appDetails != null
                                ? new Application(appDetails)
                                : new Application(g);
                    })
                    .collect(Collectors.toList());
            // TODO: save to user and allow it make cascade save
            if(!applications.isEmpty())
                applicationDataService.saveAll(applications);
        }
    }
}
