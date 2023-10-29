package ru.ylab.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.Constants;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.service.SessionService;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Логин для пользователя.
 */
@RestController
@RequiredArgsConstructor
@Validated
public class LoginController {
    private final SessionService sessionService;

    @PostMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public void loginAction(
            @CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
            @RequestBody UserLoginDto dto,
            HttpServletResponse response) {
        try {
            Optional<UUID> sessionIdFromCookie = sessionService.getUuidFromCookie(cookie);
            if (sessionIdFromCookie.isEmpty() || !sessionService.isActive(sessionIdFromCookie.get())) {
                sessionIdFromCookie.ifPresent(sessionService::logout);
                UUID sessionId = sessionService.login(dto);
                Cookie sessionCookie = new Cookie("session", sessionId.toString());
                response.addCookie(sessionCookie);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
