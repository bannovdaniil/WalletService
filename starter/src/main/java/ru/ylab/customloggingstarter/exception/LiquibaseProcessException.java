package ru.ylab.customloggingstarter.exception;

/**
 * LiquibaseProcessException - может возникнуть при инициализации liquibase
 */
public class LiquibaseProcessException extends RuntimeException {
    public LiquibaseProcessException(String message) {
        super(message);
    }
}
