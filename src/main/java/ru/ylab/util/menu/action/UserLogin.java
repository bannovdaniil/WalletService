package ru.ylab.util.menu.action;

import ru.ylab.exception.NotFoundException;
import ru.ylab.util.menu.Session;
import ru.ylab.util.menu.SessionImpl;

import java.nio.file.AccessDeniedException;
import java.util.Scanner;

public class UserLogin implements ItemAction {
    private final Session session = SessionImpl.getInstance();

    @Override
    public void execution() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter userId and password:");

        try {
            System.out.print("user ID: ");
            Long userId = Long.parseLong(scanner.next());
            System.out.print("password: ");
            String password = scanner.next();
            session.login(userId, password);
            if (session.isPresent()) {
                System.out.printf("%nHello %s,  glad to see you again. %n", session.getSessionUserName());
            }
        } catch (NumberFormatException e) {
            System.out.println("ID это число.");
        } catch (AccessDeniedException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
