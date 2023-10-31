package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.Constants;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.service.SessionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Логин для пользователя.
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {
    private final SessionService sessionService;

    @PostMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public void loginAction(
            @CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
            @RequestBody @Validated UserLoginDto dto,
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
