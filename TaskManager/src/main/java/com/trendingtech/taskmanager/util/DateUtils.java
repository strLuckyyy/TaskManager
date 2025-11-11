package com.trendingtech.taskmanager.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatNow() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}
