package com.lisadevelopment.lisa.utils;

public class StringUtils {

    public static String firstWord(String text, int offset, char separator) {
        if (offset < 0 || offset >= text.length())
            throw new StringIndexOutOfBoundsException(offset);
        StringBuilder result = new StringBuilder();
        text = text.trim();
        int i;
        for (i = offset; i < text.length(); i++) {
            if (text.charAt(i) != separator)
                result.append(text.charAt(i));
            else
                return result.toString();
        }
        return result.toString();
    }

    public static String firstWord(String text) {
        return firstWord(text, 0, ' ');
    }

    public static String firstWord(String text, char separator) {
        return firstWord(text, 0, separator);
    }

    public static String firstWord(String text, int offset) {
        return firstWord(text, offset, ' ');
    }

    public static String firstWord(String text, int offset, String separator) {
        if (offset < 0 || offset >= text.length())
            throw new StringIndexOutOfBoundsException(offset);
        StringBuilder result = new StringBuilder();
        text = text.trim();
        int i;
        for (i = offset; i < text.length(); i++) {
            if (!text.startsWith(separator, i))
                result.append(text.charAt(i));
            else
                return result.toString();
        }
        return result.toString();
    }

    public static String firstWord(String text, String separator) {
        return firstWord(text, 0, separator);
    }
}
