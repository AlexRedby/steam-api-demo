package ru.alexredby.demo.services;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexredby.demo.ApiTokenClass;
import ru.alexredby.demo.persistance.models.User;
import ru.alexredby.demo.persistance.services.UserDataService;

import javax.transaction.Transactional;

@Service
@Transactional
public class SteamExternalDataService {

    // It should be created only in one place, so it could be a bottleneck. Pay attention!
    private SteamWebApiClient steamWebApiClient;
    private SteamUser steamUser;

    private UserDataService userDataService;

    @Autowired
    public SteamExternalDataService(UserDataService userDataService) {
        this.userDataService = userDataService;
        // TODO: mb relocate it in some Bean???
        this.steamWebApiClient = new SteamWebApiClient(ApiTokenClass.API_TOKEN);
        // TODO: Don't know where it should be created... mb relocate too
        this.steamUser = new SteamUser(steamWebApiClient);
    }

    public User updateOrCreateUser(String userId) {
        // TODO: write proper code here
        return new User();
    }
}
