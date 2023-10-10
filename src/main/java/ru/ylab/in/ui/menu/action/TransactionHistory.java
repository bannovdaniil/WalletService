package ru.ylab.in.ui.menu.action;

import ru.ylab.service.TransactionService;
import ru.ylab.service.impl.TransactionServiceImpl;

public class TransactionHistory implements ItemAction {
    private TransactionService transactionService = TransactionServiceImpl.getInstance();

    @Override
    public void execution() {
        transactionService.findAll().forEach(System.out::println);
    }
}
