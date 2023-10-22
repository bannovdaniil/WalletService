package ru.ylab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import ru.ylab.Constants;

import java.time.LocalDateTime;

/**
 * Сущность аудита действий пользователя.
 * - время
 * - что делал
 * - кто делал.
 * - подробности о пользователе
 */
@Getter
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
}
