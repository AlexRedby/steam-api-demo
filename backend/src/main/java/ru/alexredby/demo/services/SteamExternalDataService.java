package ru.alexredby.demo.services;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexredby.demo.exceptions.UserNotFoundException;
import ru.alexredby.demo.persistance.models.User;
import ru.alexredby.demo.persistance.services.UserDataService;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletionException;

/**
 * Service connecting a Steam Api with inner DB.
 */
@Service
@Transactional
public class SteamExternalDataService {

    private SteamUser steamUser;

    private UserDataService userDataService;

    @Autowired
    public SteamExternalDataService(UserDataService userDataService, SteamWebApiClient steamWebApiClient) {
        this.userDataService = userDataService;
        this.steamUser = new SteamUser(steamWebApiClient);
    }

    /**
     * Updates or creates user and all related user information.
     *
     * @param userId a steam id of user to update or create.
     * @return an user which was updated or created.
     */
    // TODO: throw some exception if user not found
    public User updateOrCreateUser(@NotNull Long userId) throws UserNotFoundException, CompletionException {
        // Start request to Steam Api through CompletableFuture
        final var profileFuture = steamUser.getPlayerProfile(userId);

        @Nullable
        final SteamPlayerProfile profile = profileFuture.join();

        if (profile == null) {
            throw new UserNotFoundException("User wasn't found through Steam Api. Check id if it correct");
        }

        User user = userDataService.save(new User(profile));

        return user;
    }
}
