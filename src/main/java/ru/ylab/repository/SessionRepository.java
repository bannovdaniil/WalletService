package ru.ylab.repository;

import ru.ylab.model.Session;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для управления сессиями пользователей.
 */
public interface SessionRepository {
    /**
     * Зафиксировать сессию
     */
    Session save(Session session);

    /**
     * Получить текущую сессию
     */
    Optional<Session> findById(UUID sessionId);

    /**
     * Удалить сессию
     */
    void deleteById(UUID sessionId);

    /**
     * Проверить сессию на существование.
     */
    boolean isActive(UUID sessionId);
}
