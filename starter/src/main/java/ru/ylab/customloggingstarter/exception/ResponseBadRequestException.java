package ru.ylab.customloggingstarter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Любой не корректный запрос.
 * Выбросится из Contrellera для генерации кастомного ответа.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResponseBadRequestException extends RuntimeException {
    public ResponseBadRequestException(String message) {
        super(message);
    }
}
