package com.ahnis.servaia.user.util;


import com.ahnis.servaia.user.enums.ReportFrequency;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

//To calculate next report date we will use  this :)
public final class UserUtils {
    private UserUtils() {
        throw new UnsupportedOperationException("Cannot initialise Utility class");
    }

    public static Instant calculateNextReportOn(Instant currentDate, ReportFrequency reportFrequency) {
        return switch (reportFrequency) {
            case DAILY -> currentDate.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            case WEEKLY -> currentDate.plus(7, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            case BIWEEKLY -> currentDate.plus(14, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            case MONTHLY -> {
                // Convert Instant to ZonedDateTime to handle months
                ZonedDateTime zonedDateTime = currentDate.atZone(ZoneOffset.UTC);
                // Add 1 month
                ZonedDateTime updatedZonedDateTime = zonedDateTime.plusMonths(1);
                // Convert back to Instant(At the start of the day)
                yield updatedZonedDateTime
                        .toInstant()
                        .truncatedTo(ChronoUnit.DAYS);
            }
        };
    }
}
