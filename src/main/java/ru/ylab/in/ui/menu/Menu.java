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

/**
 * Реализация меню. Использован паттерн "Стратегия".
 * <p>
 * itemList - хранит Пункты основного меню.
 * loggedUserItemList - хранит Пункты меню пользователя.
 */
public class Menu {
    private final List<Item> itemList;
    private final List<Item> loggedUserItemList;
    private final Session session;
    private final ActionService actionService;
    private List<Item> activeItemList;

    public Menu() {
        session = SessionImpl.getInstance();
        actionService = ActionServiceImpl.getInstance();
        this.itemList = new ArrayList<>();
        this.loggedUserItemList = new ArrayList<>();
        this.activeItemList = itemList;
    }

    /**
     * Добавляем пункты меню.
     *
     * @param type - тип меню, основное | меню пользователя.
     * @param item - пункт меню с запрограммированным действием.
     */
    public void addElement(ItemType type, Item item) {
        if (item != null) {
            if (type == ItemType.MAIN_MENU) {
                itemList.add(item);
            } else {
                loggedUserItemList.add(item);
            }
        }
    }

    /**
     * Основная обратка меню.
     */
    public void doAction() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int index;
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
                            session.getUser().toString(),
                            session.getUser().orElse(null).getId()
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

    /**
     * Переключаем меню из общего в меню пользователя.
     */
    private void switchMenuItem() {
        if (session.isPresent()) {
            activeItemList = loggedUserItemList;
        } else {
            activeItemList = itemList;
        }
    }

    private void showMenu() {
        System.out.printf("%n%2$s%n>%1$s%n%2$s%n", session.getUserName(), "-".repeat(30));

        for (int i = 0; i < activeItemList.size(); i++) {
            Item item = activeItemList.get(i);
            System.out.printf("%d. %s%n", (i + 1), item.getName());
        }

        System.out.printf("%n%d. %s%n", (activeItemList.size() + 1), "Exit");

        System.out.printf("%s%n%s", "-".repeat(30), "> Select item: ");
    }

    /**
     * Выполняем выбранный пункт меню
     *
     * @param index - номер пункта.
     */
    private void doElementAction(int index) {
        activeItemList.get(index).doAction();
    }
}
