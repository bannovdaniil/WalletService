package ru.ylab.in.ui.menu.action;

import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;

import java.util.Scanner;

/**
 * Создание нового пользователя.
 */
public class UserRegistration implements ItemAction {
    private final UserService userService = UserServiceImpl.getInstance();

    private static UserIncomingDto getUserIncomingDto(Scanner scanner) {
        System.out.println("Enter User data:");
        UserIncomingDto dto = new UserIncomingDto();

        System.out.print("First Name: ");
        dto.setFirstName(scanner.next());

        System.out.print("Last Name: ");
        dto.setLastName(scanner.next());

        System.out.print("Password: ");
        dto.setPassword(scanner.next());
        return dto;
    }

    @Override
    public void execution() {
        Scanner scanner = new Scanner(System.in);

        UserIncomingDto dto = getUserIncomingDto(scanner);

        User user;
        try {
            user = userService.add(dto);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Не смог создать пользователя.");
            return;
        }
        System.out.println("New user entity: " + user);
    }

}
