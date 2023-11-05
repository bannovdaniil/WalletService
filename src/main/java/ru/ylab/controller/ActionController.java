package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.exception.ResponseAccessDeniedException;
import ru.ylab.exception.ResponseBadRequestException;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.SessionService;
import ru.ylab.util.Constants;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Показывает Лог действий пользователя.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ActionController {
    private final ActionService actionService;
    private final SessionService sessionService;

    @GetMapping(value = "/action", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Action>> getActionList(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                return ResponseEntity.ok(actionService.findAll());
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
