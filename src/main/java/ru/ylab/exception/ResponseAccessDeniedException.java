package ru.ylab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Доступ запрещен.
 * Выбросится из Contrellera для генерации кастомного ответа.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResponseAccessDeniedException extends RuntimeException {
    public ResponseAccessDeniedException(String message) {
        super(message);
    }
}
