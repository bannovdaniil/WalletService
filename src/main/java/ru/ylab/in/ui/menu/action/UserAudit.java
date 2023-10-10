package ru.ylab.in.ui.menu.action;

import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.impl.ActionServiceImpl;

import java.util.List;

/**
 * Показывает Лог действий пользователя.
 */
public class UserAudit implements ItemAction {
    private final ActionService actionService = ActionServiceImpl.getInstance();

    @Override
    public void execution() {
        List<Action> actionList = actionService.findAll();
        if (actionList.isEmpty()) {
            System.out.println("Actions not found.");
        } else {
            actionList.forEach(System.out::println);
        }
    }
}
