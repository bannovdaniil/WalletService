package ru.ylab.service;

import ru.ylab.model.Action;

import java.util.List;

public interface ActionService {
    /**
     * Добавить событие в базу
     *
     * @param action - событие
     */
    Action add(Action action);

    /**
     * Вернуть список событий.
     */
    List<Action> findAll();
}
