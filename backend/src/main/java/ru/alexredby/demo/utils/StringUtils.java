package ru.alexredby.demo.utils;

import javax.validation.constraints.NotNull;

/**
 * Contains inside different static util functions for String
 */
public class StringUtils {
    /**
     * Splits given string to 2 substrings by splitter on last entry in string
     *
     * @param str a target string to split
     * @param splitter a string which should be found in given string
     * @return an array of 2 substrings without splitter
     *         an array with original string, if splitter not found in given string
     *         an array with original string without last symbol(splitter), if splitter located on the end of given string
     */
    @NotNull
    public static String[] splitByLast(@NotNull String str, @NotNull String splitter) {
        int indexOfLast = str.lastIndexOf(splitter);

        // If splitter not found in given string - return an array with original string
        if (indexOfLast == -1) return new String[] {str};

        // If splitter located on the end of given string
        // - return an array with original string without last symbol(splitter)
        if (indexOfLast == (str.length() - 1)) return new String[] {str.substring(0, indexOfLast)};

        return new String[] {
                str.substring(0, indexOfLast),
                str.substring(indexOfLast + 1)
        };
    }
}
