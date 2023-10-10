package ru.ylab.in.ui.menu;

import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.impl.ActionServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final List<Item> itemList;
    private final List<Item> loggedUserItemList;
    private final Session session = SessionImpl.getInstance();
    private final ActionService actionService = ActionServiceImpl.getInstance();
    private List<Item> activeItemList;

    public Menu() {
        this.itemList = new ArrayList<>();
        this.loggedUserItemList = new ArrayList<>();
        this.activeItemList = itemList;
    }

    public void addElement(ItemType type, Item item) {
        if (item != null) {
            if (type == ItemType.MAIN_MENU) {
                itemList.add(item);
            } else {
                loggedUserItemList.add(item);
            }
        }
    }

    public void doAction() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int index = -1;
            showMenu();
            try {
                index = Integer.parseInt(scanner.next());
                index--;
                if (index == activeItemList.size()) {
                    break;
                }
                if (index < 0 || index > activeItemList.size()) {
                    System.err.println("Не существует такого пункта, повторите ввод.");
                } else {
                    doElementAction(index);

                    actionService.add(new Action(
                            LocalDateTime.now(),
                            activeItemList.get(index).getName(),
                            session.getUser().orElse(null)
                    ));
                    switchMenuItem();
                }
            } catch (NumberFormatException e) {
                System.err.println("Понимаю только числа.");
            }
        }
        System.out.println("Bye, I will be back!");
        scanner.close();
    }

    private void switchMenuItem() {
        if (session.isPresent()) {
            activeItemList = loggedUserItemList;
        } else {
            activeItemList = itemList;
        }
    }

    private void showMenu() {
        System.out.printf("%n%2$s%n>%1$s%n%2$s%n",
                session.getUserName(),
                "-".repeat(30));

        for (int i = 0; i < activeItemList.size(); i++) {
            Item item = activeItemList.get(i);
            System.out.printf("%d. %s%n", (i + 1), item.getName());
        }

        System.out.printf("%n%d. %s%n", (activeItemList.size() + 1), "Exit");

        System.out.printf("%s%n%s",
                "-".repeat(30),
                "> Select item: ");
    }

    private void doElementAction(int index) {
        activeItemList.get(index).doAction();
    }
}
