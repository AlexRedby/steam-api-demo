package ru.alexredby.demo.models;

import javax.annotation.Nullable;

/**
 * Make connection between Steam Api communityVisibilityState from SteamPlayerProfile and this program
 *
 * 1 - Private
 * 2 - Friends only
 * 3 - Public
 */
public enum UserVisibility {
    PRIVATE(1),
    FRIEND_ONLY(2),
    PUBLIC(3);

    /** Code of user visibility state from Steam API */
    int code;

    UserVisibility(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * Converts integer code of visibility state, taken from steam api, to enum
     *
     * @param code is code of visibility state, taken from steam api
     * @return enum value appropriate to given code, or null if couldn't find matches
     */
    @Nullable
    public static UserVisibility fromCode(int code) {
        for (UserVisibility type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}