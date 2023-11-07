package ru.ylab.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.customloggingstarter.aop.annotation.ActionLogger;
import ru.ylab.exception.ResponseAccessDeniedException;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.service.SessionService;
import ru.ylab.util.Constants;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Авторизация Login/Logout для пользователя.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final SessionService sessionService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ActionLogger
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

    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    @ActionLogger
    public void logoutAction(
            @CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
            HttpServletResponse response) {
        try {
            Optional<UUID> sessionIdFromCookie = sessionService.getUuidFromCookie(cookie);
            if (sessionIdFromCookie.isPresent() && sessionService.isActive(sessionIdFromCookie.get())) {
                sessionService.logout(sessionIdFromCookie.get());
                Cookie sessionCookie = new Cookie("session", "");
                sessionCookie.setMaxAge(0);
                response.addCookie(sessionCookie);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            throw new ResponseAccessDeniedException(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
