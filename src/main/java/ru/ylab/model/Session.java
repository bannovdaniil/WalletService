package ru.ylab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.ylab.Constants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность сессий пользователя.
 * - время
 * - токен
 * - время авторизации.
 * - подробности о пользователе
 */
public class Session {
    private final UUID uuid;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private final LocalDateTime time;
    private final Long userId;

    public Session(UUID uuid, LocalDateTime time, Long userId) {
        this.uuid = uuid;
        this.time = time;
        this.userId = userId;
    }

    public Session(LocalDateTime time, Long userId) {
        this.uuid = null;
        this.time = time;
        this.userId = userId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Long getUserId() {
        return userId;
    }
}
