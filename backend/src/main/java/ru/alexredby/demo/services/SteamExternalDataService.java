package ru.alexredby.demo.services;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexredby.demo.ApiTokenClass;
import ru.alexredby.demo.persistance.models.User;
import ru.alexredby.demo.persistance.services.UserDataService;

import javax.transaction.Transactional;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class SteamExternalDataService {

    // It should be created only in one place, so it could be a bottleneck. Pay attention!
    //private SteamWebApiClient steamWebApiClient;
    private SteamUser steamUser;

    private UserDataService userDataService;

    @Autowired
    public SteamExternalDataService(UserDataService userDataService, SteamWebApiClient steamWebApiClient) {
        this.userDataService = userDataService;
        // TODO: mb relocate it in some Bean???
        //this.steamWebApiClient = steamWebApiClient;
        // TODO: Don't know where it should be created... mb relocate too
        this.steamUser = new SteamUser(steamWebApiClient);
    }

    /**
     * Updates or creates user and all related user information
     *
     * @param userId a steam id of user to update or create
     * @return an user which was updated
     */
    // TODO: throw some exception if user not found
    public User updateOrCreateUser(Long userId) {
        // Start request to Steam Api through CompletableFuture
        var profileFuture = steamUser.getPlayerProfile(userId);

        User user = null;
        try {
            SteamPlayerProfile profile = profileFuture.get();
            user = userDataService.save(new User(profile));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return user;
    }
}
