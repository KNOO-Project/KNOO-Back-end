package com.woopaca.knoo.common;

import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public static DateTimeFormatter getFormatter() {
        return FORMATTER;
    }
}
