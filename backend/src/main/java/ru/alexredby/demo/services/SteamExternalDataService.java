package ru.alexredby.demo.services;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamPlayerService;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamStorefront;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUserStats;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexredby.demo.enums.UserVisibility;
import ru.alexredby.demo.exceptions.UserNotFoundException;
import ru.alexredby.demo.persistance.models.Achievement;
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
    private SteamUserStats steamUserStats;

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
        this.steamUserStats = new SteamUserStats(steamWebApiClient);
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
    // TODO: lunch this on background and return info to user immediately
    private void updateGamesOf(@NotNull User user) {
        // Check if profile is public. If it is - update, otherwise do nothing
        if (user.getCommunityVisibilityState() == UserVisibility.PUBLIC) {
            CompletableFuture<List<SteamPlayerOwnedGame>> gamesFuture = null;
            // Check if 2 weeks passed after last update
            if (ChronoUnit.WEEKS.between(user.getLastUpdate(), LocalDateTime.now()) >= 2) {
                gamesFuture = steamPlayerService.getOwnedGames(user.getId(), true, true);
            } else {
                // Count = 0 means all games
                gamesFuture = steamPlayerService.getRecentlyPlayedGames(user.getId(), 0).thenApply(list ->
                        list == null ? null : list.stream()
                                .map(SteamUtils::convertRecentGamesToOwned)
                                .collect(Collectors.toList())
                );
            }
            @Nullable
            List<SteamPlayerOwnedGame> games = gamesFuture.join();

            // TODO: Replace it
            if (games != null) {
                // Checks if game already in DB
                List<Application> existingApplications = applicationDataService.findAllById(
                        games.stream()
                                .map(SteamPlayerOwnedGame::getAppId)
                                .collect(Collectors.toList())
                );

                // Deletes games found in DB from list
                existingApplications.forEach(app -> games.removeIf(g -> app.getId() == g.getAppId()));

                // Gets new game from Steam Api and map them in Application list
                List<Application> newApplications = games.stream()
                        .map(g -> {
                            @Nullable
                            StoreAppDetails appDetails = steamStorefront.getAppDetails(g.getAppId()).join();
                            Application application = appDetails != null
                                    ? new Application(appDetails)
                                    : new Application(g);
                            if (application.isHasAchievements()) updateAchievementsOf(application);
                            return application;
                        }).collect(Collectors.toList());

                // TODO: save to user and allow it make cascade save
                if (!newApplications.isEmpty())
                    //newApplications.forEach(app -> applicationDataService.save(app));
                    applicationDataService.saveAll(newApplications);
            }
        }
    }

    private void updateAchievementsOf(@NotNull Application application) {
        @Nullable
        final var statsAndAchievements = steamUserStats.getSchemaForGame(application.getId()).join();

        if(statsAndAchievements != null) {
            final var percentages = steamUserStats.getGlobalAchievementPercentagesForApp(application.getId()).join();
            var achievements = statsAndAchievements.getAchievementSchemaList().stream()
                    .map(ach -> {
                        var foundPercentage = percentages.stream()
                                .filter(per -> per.getName().equals(ach.getName()))
                                .findFirst()
                                .orElse(null);
                        return foundPercentage != null
                                ? new Achievement(application, ach, foundPercentage.getPercentage())
                                : new Achievement(application, ach, 0);
                    }).collect(Collectors.toList());

            application.setAchievements(achievements);
        }
    }
}
