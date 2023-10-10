package ru.ylab.in.ui;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

/**
 * Сессия используется для авторизации пользователя.
 */
public interface Session {
    /**
     * Проверяет активна ли текущая сессия.
     */
    boolean isPresent();

    /**
     * Получаем имя авторизированного пользователя.
     */
    String getUserName();

    /**
     * Получить сущность авторизированного пользователя.
     */
    Optional<User> getUser();

    /**
     * Получить счет авторизированного пользователя.
     */
    Wallet getUserWallet();

    /**
     * Обновить состояние счета авторизированного пользователя.
     */
    void setUserWallet(Wallet wallet);

    /**
     * Обеспечивает авторизацию пользователя в системе.
     *
     * @param userId   - id пользователя
     * @param password - пароль пользователя
     * @throws AccessDeniedException - если пароль не правильный
     * @throws NotFoundException     - если нет такого id в базе.
     */
    void login(Long userId, String password) throws AccessDeniedException, NotFoundException;

    /**
     * Завершение сессии.
     */
    void logout();
}
