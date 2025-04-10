package com.ahnis.servaia.user.enums;

import com.ahnis.servaia.user.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    USER, ADMIN;
    @JsonCreator
    public static Role fromString(String value) {
        return EnumUtils.fromString(Role.class, value);
    }


}
