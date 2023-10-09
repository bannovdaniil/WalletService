package ru.ylab.util.menu.action;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;

import java.util.Scanner;

public class UserFindById implements ItemAction {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execution() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter User ID:");
        try {
            Long id = Long.parseLong(scanner.next());
            User user = userService.find(id);
            System.out.println(user);
        } catch (NotFoundException e) {
            System.out.println("Пользователь с таким ID не существует.");
        } catch (NumberFormatException e) {
            System.out.println("ID это число.");
        }
    }
}
