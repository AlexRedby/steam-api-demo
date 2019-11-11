package ru.alexredby.demo.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "USERS")
@Data
public class User {
    /** 64bit Steam Id */
    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    /** A field that describes the access setting of the profile */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserVisibility communityVisibilityState;

    /** If set to 1 the user has configured the profile */
    private int profileState;

    /** Nickname */
    @Column(nullable = false)
    private String nickname;

    /**
     * Steam Url to 32x32 user's avatar image
     *
     * For 32x32 avatar use getter getSmallAvatar
     * For 64x64 avatar use getter getMediumAvatar
     * For 184x184 avatar use getter getFullAvatar
     */
    private String smallAvatar;

    /**
     * The player's primary group, as configured in their Steam Community profile
     * Alias: primaryclanid
     * Visibility in steam: Private
     */
    private long primaryGroupId;

    /**
     * The time the player's account was created
     * Note: It's UNIX time in steam api
     * Visibility in steam: Private
     */
    private Instant timeCreated;

    /**
     * Constructor for convert SteamPlayerProfile from com.ibasco.agql to inner User
     */
    public User(@NotNull SteamPlayerProfile profile) {
        this.id = profile.getSteamId();
        this.communityVisibilityState = UserVisibility.fromCode(profile.getCommunityVisibilityState());
        this.nickname = profile.getName();
        this.profileState = profile.getProfileState();
        this.smallAvatar = profile.getProfileUrl();
        this.primaryGroupId = profile.getPrimaryGroupId();
        this.timeCreated = Instant.ofEpochSecond(profile.getTimeCreated());
    }

    /**
     * @return url to Steam profile of User
     */
    public String getProfileUrl() {
        return "https://steamcommunity.com/profiles/" + id;
    }

    /**
     * @return url to 64x64 user's avatar
     */
    public String getMediumAvatar() {
        return String.join("_medium.", smallAvatar.split("."));
    }

    /**
     * @return url to 184x184 user's avatar
     */
    public String getFullAvatar() {
        return String.join("_full.", smallAvatar.split("."));
    }
}
