package ru.ylab.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ylab.Constants;
import ru.ylab.exception.ErrorHeader;
import ru.ylab.model.dto.BalanceDto;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.WalletService;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Работа со Счетом пользователя
 * - Получить баланс
 * - Изменить баланс
 */
@RestController
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final SessionService sessionService;

    /**
     * Получить баланс пользователя
     */
    @GetMapping(value = "/api/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("squid:S3740")
    public ResponseEntity getWalletBalance(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                BalanceDto balanceDto = walletService.getBalance(sessionId.get());
                return ResponseEntity.ok(balanceDto);
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(new ErrorHeader(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(new ErrorHeader(e.getMessage()));
        }
    }

    /**
     * Изменить баланс пользователя
     */
    @PutMapping(value = "/api/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("squid:S3740")
    public ResponseEntity changeBalance(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
                                        @RequestParam WalletIncomingDto incomingDto) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {

                BalanceDto balanceDto = walletService.changeBalance(sessionId.get(), incomingDto);
                return ResponseEntity.ok(balanceDto);
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
