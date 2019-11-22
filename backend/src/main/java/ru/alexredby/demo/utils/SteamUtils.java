package ru.alexredby.demo.utils;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerRecentPlayed;

public class SteamUtils {

    public static SteamPlayerOwnedGame convertRecentGamesToOwned(SteamPlayerRecentPlayed game) {
        var ownedGames = new SteamPlayerOwnedGame();

        ownedGames.setAppId(game.getAppId());
        ownedGames.setName(game.getName());
        ownedGames.setTotalPlaytime(game.getTotalPlaytime());
        ownedGames.setIconImgUrl(game.getImgIconUrl());
        ownedGames.setLogoImgUrl(game.getImgLogoUrl());

        return ownedGames;
    }
}
