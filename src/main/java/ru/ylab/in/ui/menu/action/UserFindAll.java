package ru.ylab.in.ui.menu.action;

import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;

/**
 * Выводит на экран всех зарегистрированных пользователей
 */
public class UserFindAll implements ItemAction {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execution() {
        userService.findAll().forEach(System.out::println);
    }
}
