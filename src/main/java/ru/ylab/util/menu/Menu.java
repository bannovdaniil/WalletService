package ru.ylab.util.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final List<Item> itemList;
    private final Session session = SessionImpl.getInstance();

    public Menu() {
        this.itemList = new ArrayList<>();
    }

    public void addElement(Item item) {
        if (item != null) {
            itemList.add(item);
        }
    }

    public void doAction() {
        Scanner scanner = new Scanner(System.in);

        int index = -1;
        while (index < 0 || index != itemList.size()) {
            showMenu();
            try {
                index = Integer.parseInt(scanner.next());
                index--;
                if (index < 0 || index > itemList.size()) {
                    System.out.println("Не существует такого пункта, повторите ввод.");
                    index = -1;
                }
            } catch (NumberFormatException e) {
                index = -1;
            }
            if ((index < 0 || index != itemList.size())) {
                doElementAction(index);
            }
        }
        System.out.println("Bye, I will be back!");
        scanner.close();
    }

    private void showMenu() {
        System.out.printf("%n%2$s%n>%1$s%n- Menu:%n%2$s%n%n",
                session.getSessionUserName(),
                "-".repeat(30));

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            System.out.printf("%d. %s%n", (i + 1), item.getName());
        }

        System.out.printf("%n%d. %s%n", (itemList.size() + 1), "Exit");

        System.out.printf("%s%n%s",
                "-".repeat(30),
                "> Select item: ");
    }

    private void doElementAction(int index) {
        itemList.get(index).doAction();
    }
}
