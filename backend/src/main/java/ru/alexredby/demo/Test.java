package ru.alexredby.demo;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import ru.alexredby.demo.steam.ApiTokenClass;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        SteamWebApiClient apiClient = new SteamWebApiClient(ApiTokenClass.API_TOKEN);
        SteamUser steamUser = new SteamUser(apiClient);

        var profile = steamUser.getPlayerProfile(76561198015294053L).join();
        System.out.println("avatarUrl = " + profile.getAvatarUrl() +
                ", name = " + profile.getName() +
                ", steamId = " + profile.getSteamId() +
                ", groupId = " + profile.getPrimaryGroupId());

        apiClient.close();
    }
}
