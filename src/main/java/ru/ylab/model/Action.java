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
    private final User user;

    public Action(LocalDateTime time, String userAction, User user) {
        this.time = time;
        this.userAction = userAction;
        this.user = user;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Action.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("time=" + time)
                .add("action='" + userAction + "'")
                .add("user=" + user)
                .toString();
    }
}
