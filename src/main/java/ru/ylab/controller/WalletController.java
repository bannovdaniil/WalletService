package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.exception.ResponseAccessDeniedException;
import ru.ylab.exception.ResponseBadRequestException;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.model.dto.WalletOutDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.WalletService;
import ru.ylab.util.Constants;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Работа со Счетом пользователя
 * - Получить баланс
 * - Изменить баланс
 */
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final SessionService sessionService;

    /**
     * Получить баланс пользователя
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletOutDto> getWalletBalance(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                return ResponseEntity.ok(walletService.getBalance(sessionId.get()));
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            throw new ResponseAccessDeniedException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseBadRequestException(e.getMessage());
        }
    }

    /**
     * Изменить баланс пользователя
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletOutDto> changeBalance(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
                                                      @RequestBody @Validated WalletIncomingDto incomingDto) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                return ResponseEntity.ok(walletService.changeBalance(sessionId.get(), incomingDto));
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            throw new ResponseAccessDeniedException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseBadRequestException(e.getMessage());
        }
    }

}
