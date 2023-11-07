package ru.ylab.exception;

/**
 * DatabaseConnectionException - Проблемы возникшие при соединении с базой
 */
public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
