package ru.ylab.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.Constants;
import ru.ylab.exception.ErrorHeader;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.UserService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Работа с Пользователем
 * - Регистрация
 * - Информации по ID
 * - Получить Список пользователей
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    /**
     * Получить пользователя по ID
     */
    @GetMapping(value = "/api/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("squid:S3740")
    public ResponseEntity getUserById(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
                                      @PathVariable Long userId) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                UserOutDto dto = userService.findById(userId);
                return ResponseEntity.ok(dto);
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
     * Получить весь список Пользователей
     */
    @GetMapping(value = "/api/user/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("squid:S3740")
    public ResponseEntity getUserList(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                List<UserOutDto> userList = userService.findAll();
                return ResponseEntity.ok(userList);
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
     * Создать пользователя
     */
    @PostMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("squid:S3740")
    public ResponseEntity createUser(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
                                     @RequestBody @Validated UserIncomingDto userIncomingDto) {
        try {
            UserOutDto userOutDto = userService.add(userIncomingDto);
            return ResponseEntity.ok(userOutDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(new ErrorHeader(e.getMessage()));
        }
    }

}
