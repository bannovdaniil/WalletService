package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.Constants;
import ru.ylab.service.SessionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Логаут для пользователя.
 */
@RestController
@RequiredArgsConstructor
@Validated
public class LogoutController {
    private final SessionService sessionService;

    @GetMapping(value = "/api/logout", produces = MediaType.APPLICATION_JSON_VALUE)
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
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
