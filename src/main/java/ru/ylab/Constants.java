package ru.ylab;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String REGEXP_FORMAT_MONEY = "^\\d*([\\.,]\\d{1,2})?$";
    public static final String SESSION_COOKIE = "session";
    public static final String DATE_TIME_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_SPACE = DateTimeFormatter.ofPattern(DATE_TIME_STRING);

    private Constants() {
    }
}