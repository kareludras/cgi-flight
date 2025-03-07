package com.example.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatUtil {

    private static final DateTimeFormatter FLIGHT_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm");

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(FLIGHT_TIME_FORMATTER);
    }
}