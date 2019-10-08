package ru.alexredby.demo.models;

import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamPlayerService;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUserStats;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerAchievement;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class User {
    private String steamId;
    private int communityVisibilityState;
    private int profileState;
    private String name;
    private long lastLogOff;
    // Steam id could use instead storing link
    // https://steamcommunity.com/profiles/{steam_id}/
    private String profileUrl;
    // For medium/full image just add "_medium" and "_full" accordingly before ".jpg"
    private String avatarUrl;
    private int personaState;
    private long primaryGroupId;
    private long timeCreated;
    private int personaStateFlags;
}
