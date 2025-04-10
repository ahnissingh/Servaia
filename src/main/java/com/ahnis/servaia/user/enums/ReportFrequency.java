package com.ahnis.servaia.user.enums;

import com.ahnis.servaia.user.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReportFrequency {
    DAILY, WEEKLY, BIWEEKLY, MONTHLY;

    @JsonCreator
    public static ReportFrequency fromString(String value) {
        return EnumUtils.fromString(ReportFrequency.class, value);
    }
}
