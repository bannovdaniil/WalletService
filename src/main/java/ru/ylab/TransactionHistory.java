package ru.ylab;

import ru.ylab.in.ui.menu.action.ItemAction;

public class TransactionHistory implements ItemAction {
    @Override
    public void execution() {
        System.out.println("transaction");
    }
}
