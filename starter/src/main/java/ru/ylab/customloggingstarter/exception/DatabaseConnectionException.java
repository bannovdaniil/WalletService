package ru.ylab.customloggingstarter.exception;

/**
 * DatabaseConnectionException - Проблемы возникшие при соединении с базой
 */
public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
