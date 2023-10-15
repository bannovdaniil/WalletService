package ru.ylab.model;

import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * Сущность аудита действий пользователя.
 * - время
 * - что делал
 * - кто делал.
 */
public class Action extends Entity {
    private final LocalDateTime time;
    private final String userAction;
    private final Long userId;
    private final String information;

    public Action(LocalDateTime time, String userAction, Long userId, String information) {
        this.time = time;
        this.userAction = userAction;
        this.information = information;
        this.userId = userId;
    }

    public Action(Long id, LocalDateTime time, String userAction, Long userId, String information) {
        super.setId(id);
        this.time = time;
        this.userAction = userAction;
        this.information = information;
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getUserAction() {
        return userAction;
    }

    public String getInformation() {
        return information;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Action.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("time=" + time)
                .add("action='" + userAction + "'")
                .add("userId=" + userId)
                .add("information='" + information)
                .toString();
    }
}
