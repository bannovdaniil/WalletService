package ru.ylab.model;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Action extends Entity {
    private LocalDateTime time;
    private String userAction;
    private User user;

    public Action(LocalDateTime time, String userAction, User user) {
        this.time = time;
        this.userAction = userAction;
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getUserAction() {
        return userAction;
    }

    public User getUser() {
        return user;
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
