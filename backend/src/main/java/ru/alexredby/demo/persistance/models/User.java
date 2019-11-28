package ru.alexredby.demo.persistance.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alexredby.demo.enums.UserVisibility;
import ru.alexredby.demo.utils.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

// TODO: make common class with id mb? research this question
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Data
public class User implements Serializable {
    /** 64bit Steam Id */
    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

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
    private Long primaryGroupId;

    /**
     * The time the player's account was created
     * Note: It's UNIX time in steam api
     * Visibility in steam: Private
     */
    private Instant timeCreated;

    /**
     * Last time when profile was update with all related information
     */
    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    /**
     * List of user's applications with achievements and totalPlaytime
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserApplication> applications;

    /**
     * Constructor for User if known only id
     * Note: use {@code update()} to insert rest of info later
     */
    public User(@NotNull Long id) {
        this.id = id;
        this.primaryGroupId = null;
        this.timeCreated = null;
        this.lastUpdate = LocalDateTime.MIN;
        this.applications = new ArrayList<>();
    }

    /**
     * Constructor for convert SteamPlayerProfile from com.ibasco.agql to inner User
     *
     * @throws NumberFormatException profile steam id is not long.
     */
    public User(@NotNull SteamPlayerProfile profile) throws NumberFormatException {
        this(Long.parseLong(profile.getSteamId()));
        update(profile);
    }

    /**
     * Updates profile info to new one from {@code profile}
     *
     * @param profile to update
     */
    public void update(@NotNull SteamPlayerProfile profile) {
        this.communityVisibilityState = UserVisibility.fromCode(profile.getCommunityVisibilityState());
        this.nickname = profile.getName();
        this.profileState = profile.getProfileState();
        this.smallAvatar = profile.getAvatarUrl();

        Long primaryGroupId = this.primaryGroupId;
        Instant timeCreated = this.timeCreated;
        // If profile is public then set private info, otherwise - null
        if (this.communityVisibilityState == UserVisibility.PUBLIC) {
            primaryGroupId = profile.getPrimaryGroupId();
            timeCreated = Instant.ofEpochSecond(profile.getTimeCreated());
        }
        this.primaryGroupId = primaryGroupId;
        this.timeCreated = timeCreated;
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
        return String.join("_medium.", StringUtils.splitByLast(smallAvatar, "."));
    }

    /**
     * @return url to 184x184 user's avatar
     */
    public String getFullAvatar() {
        return String.join("_full.", StringUtils.splitByLast(smallAvatar, "."));
    }

    public void setApplications(List<UserApplication> applications) {
        applications.forEach(a -> a.setUser(this));
        this.applications = applications;
    }
}
