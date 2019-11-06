package ru.alexredby.demo.models;

import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamPlayerService;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamUser;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import lombok.Data;

import javax.persistence.Entity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

//@Entity
@Data
public class User {
    /** 64bit Steam Id */
    private String id;

    // TODO: make enum
    /**
     * An integer that describes the access setting of the profile
     * 1 - Private
     * 2 - Friends only
     * 3 - Public
     */
    private int communityVisibilityState;

    /** If set to 1 the user has configured the profile */
    private int profileState;

    /** Nickname */
    private String name;

    // Who cares...
    //private long lastLogOff;

    // Steam id could use instead storing link
    // https://steamcommunity.com/profiles/{steam_id}/
    //private String profileUrl;

    /**
     * Steam Url to avatar image
     * For 32x32 avatar use this Url
     * For 64x64 avatar add "_medium" before ".jpg" to Url
     * For 184x184 avatar add "_full" before ".jpg" to Url
     */
    // For medium/full image just add "_medium" and "_full" accordingly before ".jpg"
    private String avatarUrl;
    //private String avatarMediumUrl;
    //private String avatarFullUrl;

    // No need to store
    // 0 - Offline, 1 - Online, 2 - Busy, 3 - Away, 4 - Snooze, 5 - looking to trade, 6 - looking to play
    //private int personaState;

    /**
     * The player's primary group, as configured in their Steam Community profile
     * Alias: primaryclanid
     * Visibility: Private
     */
    private long primaryGroupId;

    /**
     * The time the player's account was created
     * Note: It's UNIX time in steam api
     * Visibility: Private
     */
    private LocalDateTime timeCreated;

    public User(SteamPlayerProfile profile) {
        this.id = profile.getSteamId();
        this.communityVisibilityState = profile.getCommunityVisibilityState();
        this.profileState = profile.getProfileState();
        this.avatarUrl = profile.getProfileUrl();
        this.primaryGroupId = profile.getPrimaryGroupId();
        // TODO: change it
        this.timeCreated = LocalDateTime.ofInstant(Instant.ofEpochSecond(profile.getTimeCreated()), ZoneId.of("Z"));
    }
}
