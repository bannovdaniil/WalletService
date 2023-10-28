package ru.ylab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.ylab.Constants;

import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * Сущность аудита действий пользователя.
 * - время
 * - что делал
 * - кто делал.
 * - подробности о пользователе
 */
public class Action {
    private final Long id;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private final LocalDateTime time;
    private final String userAction;
    private final Long userId;
    private final String information;

    public Action(LocalDateTime time, String userAction, Long userId, String information) {
        this.id = null;
        this.time = time;
        this.userAction = userAction;
        this.information = information;
        this.userId = userId;
    }

    public Action(Long id, LocalDateTime time, String userAction, Long userId, String information) {
        this.id = id;
        this.time = time;
        this.userAction = userAction;
        this.information = information;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getUserAction() {
        return userAction;
    }

    public Long getUserId() {
        return userId;
    }

    public String getInformation() {
        return information;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Action.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("time=" + time)
                .add("userAction='" + userAction + "'")
                .add("userId=" + userId)
                .add("information='" + information + "'")
                .toString();
    }
}
