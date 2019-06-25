package com.lisadevelopment.lisa.utils;

public class DiscordUtils {

    public static long parseSnowflake(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("The snowflake text cannot be empty");
        try {
            if (text.startsWith("-"))
                return Long.parseLong(text);
            else
                return Long.parseUnsignedLong(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("The format of the text isn't a proper long");
        }
    }
}
