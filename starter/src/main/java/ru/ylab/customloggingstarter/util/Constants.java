package ru.ylab.customloggingstarter.util;

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

    public static final String DRIVER_CLASS_KEY = "db.driver-class-name";
    public static final String URL_KEY = "DATASOURCE_URL";
    public static final String USERNAME_KEY = "POSTGRES_USER";
    public static final String PASSWORD_KEY = "POSTGRES_PASSWORD";
    public static final String DEFAULT_SCHEMA_NAME = "liquibase.defaultSchemaName";

    private Constants() {
    }
}