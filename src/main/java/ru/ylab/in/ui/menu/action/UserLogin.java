package ru.ylab.in.ui.menu.action;

import ru.ylab.exception.NotFoundException;
import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;

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
                System.out.printf("%nHello %s,  glad to see you again. %n", session.getUserName());
            }
        } catch (NumberFormatException e) {
            System.err.println("ID это число.");
        } catch (AccessDeniedException | NotFoundException e) {
            System.err.println(e.getMessage());
        }

    }
}
