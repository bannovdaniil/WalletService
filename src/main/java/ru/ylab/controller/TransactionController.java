package ru.ylab.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.Constants;
import ru.ylab.exception.ErrorHeader;
import ru.ylab.model.Transaction;
import ru.ylab.service.SessionService;
import ru.ylab.service.TransactionService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Показывает Историю транзакций.
 */
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final SessionService sessionService;

    @GetMapping(value = "/api/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("squid:S3740")
    public ResponseEntity getTransactionList(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                List<Transaction> transactionList = transactionService.findAll();
                return ResponseEntity.ok(
                        transactionList
                );
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(new ErrorHeader(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(new ErrorHeader(e.getMessage()));
        }
    }
}
