package ru.ylab.util.menu;

import ru.ylab.util.menu.action.ItemAction;

public class Item {
    private final String name;
    private final ItemAction action;

    public Item(String name, ItemAction action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void doAction() {
        action.execution();
    }
}
