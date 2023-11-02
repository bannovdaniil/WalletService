package ru.ylab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ylab.controller.*;

import java.security.InvalidParameterException;

@RestControllerAdvice(assignableTypes = {
        ActionController.class,
        AuthController.class,
        TransactionController.class,
        UserController.class,
        WalletController.class
})
public class ErrorHandler {
    @SuppressWarnings("S1068")
    private ErrorResponse errorResponse;

    @ExceptionHandler({ResponseAccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ResponseBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            InvalidParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadValidation(final Exception e) {
        return new ErrorResponse("Ошибка валидации данных: " + e.getMessage());
    }


    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
