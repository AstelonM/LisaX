package com.lisaxdevelopment.lisax.utils;

import java.util.Arrays;

public enum Format {

    MENTION(1),
    ID(2),
    NAME(3),
    NICKNAME(4),
    EFFECTIVE_NAME(5);

    private int rawValue;

    Format(int offset) {
        rawValue =  1 << offset;
    }

    public int getRawValue() {
        return rawValue;
    }

    public boolean isPresent(int rawFormats) {
        return (rawValue & rawFormats) == rawValue;
    }

    public static int getValues(Format... formats) {
        return Arrays.stream(formats).mapToInt(Format::getRawValue).reduce((v1, v2) -> v1 | v2).orElse(0);
    }
}
