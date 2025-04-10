package com.ahnis.servaia.user.enums;

import com.ahnis.servaia.user.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Language {
    ENGLISH,
    HINDI,
    PUNJABI,
    SPANISH,
    ITALIAN,
    GERMAN,
    FRENCH,
    RUSSIAN;

    @JsonCreator
    public static Language fromString(String value) {
        return EnumUtils.fromString(Language.class, value);
    }
}
