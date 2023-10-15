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
    private final String information;
    private final Long userId;

    public Action(LocalDateTime time, String userAction, String information, Long userId) {
        this.time = time;
        this.userAction = userAction;
        this.information = information;
        this.userId = userId;
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
