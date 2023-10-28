package ru.ylab;

/**
 * Константы:
 * REGEXP_FORMAT_MONEY - денежный формат
 * SESSION_COOKIE - наименование куки для сессии
 * DATE_TIME_STRING - отображение даты для json
 */
public class Constants {
    public static final String REGEXP_FORMAT_MONEY = "^\\d*([\\.,]\\d{1,2})?$";
    public static final String SESSION_COOKIE = "session";
    public static final String DATE_TIME_STRING = "yyyy-MM-dd HH:mm:ss";

    private Constants() {
    }
}