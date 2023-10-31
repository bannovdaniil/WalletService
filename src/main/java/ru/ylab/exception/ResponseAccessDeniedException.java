package ru.ylab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResponseAccessDeniedException extends RuntimeException {
    public ResponseAccessDeniedException(String message) {
        super(message);
    }

}
