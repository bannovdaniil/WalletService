package ru.ylab.exception;

/**
 * Сообщения об ошибках, для web
 */
public class ErrorHeader {
    private String error;

    public ErrorHeader() {
    }

    public ErrorHeader(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
