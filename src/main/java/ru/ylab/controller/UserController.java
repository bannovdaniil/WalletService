package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.exception.ResponseAccessDeniedException;
import ru.ylab.exception.ResponseBadRequestException;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.UserService;
import ru.ylab.util.Constants;

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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    /**
     * Получить пользователя по ID
     */
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserOutDto> getUserById(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
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
            throw new ResponseAccessDeniedException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseBadRequestException(e.getMessage());
        }
    }

    /**
     * Получить весь список Пользователей
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserOutDto>> getUserList(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie) {
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                List<UserOutDto> userList = userService.findAll();
                return ResponseEntity.ok(userList);
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
     * Создать пользователя
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserOutDto> createUser(@CookieValue(value = Constants.SESSION_COOKIE, defaultValue = "") String cookie,
                                                 @RequestBody @Validated UserIncomingDto userIncomingDto) {
        try {
            UserOutDto userOutDto = userService.add(userIncomingDto);
            return ResponseEntity.ok(userOutDto);
        } catch (Exception e) {
            throw new ResponseBadRequestException(e.getMessage());
        }
    }

}
