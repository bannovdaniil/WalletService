package ru.ylab.service;

import ru.ylab.model.Action;

import java.util.List;

public interface ActionService {
    Action add(Action action);

    List<Action> findAll();
}
