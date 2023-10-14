package ru.ylab.exception;

public class LiquibaseProcessException extends RuntimeException {
    public LiquibaseProcessException(String message) {
        super(message);
    }
}
