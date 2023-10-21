package ru.ylab.service;

import jakarta.servlet.http.Cookie;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.UserLoginDto;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Сессия используется для авторизации пользователя.
 */
public interface SessionService {

    /**
     * Получаем авторизованного пользователя.
     */
    User getUser(UUID sessionId);

    /**
     * Получить счет авторизированного пользователя.
     */
    Wallet getUserWallet(UUID sessionId);

    /**
     * Обеспечивает авторизацию пользователя в системе.
     *
     * @param dto - dto для логина пользователя
     * @return
     * @throws AccessDeniedException - если пароль не правильный
     * @throws NotFoundException     - если нет такого id в базе.
     */
    UUID login(UserLoginDto dto) throws AccessDeniedException;

    /**
     * Завершение сессии.
     */
    void logout(UUID sessionId);

    /**
     * Проверяет активна ли текущая сессия.
     */
    boolean isActive(UUID sessionId);

    /**
     * Получить ID сессии из cookie
     */
    Optional<UUID> getUuidFromCookie(Cookie[] cookies);
}
