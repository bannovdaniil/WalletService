package ru.ylab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Action {
    private Long id;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime time;
    private String userAction;
    private Long userId;
    private String information;

    public Action(LocalDateTime time, String userAction, Long userId, String information) {
        this.id = null;
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
                .add("userAction='" + userAction + "'")
                .add("userId=" + userId)
                .add("information='" + information + "'")
                .toString();
    }
}
