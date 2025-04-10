package com.ahnis.servaia.user.enums;


import com.ahnis.servaia.user.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ThemePreference {
    LIGHT, DARK;
    @JsonCreator
    public static ThemePreference fromString(String value) {
        return EnumUtils.fromString(ThemePreference.class, value);
    }
}

