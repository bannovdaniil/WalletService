package ru.ylab.in.ui.menu.action;

import ru.ylab.model.Transaction;
import ru.ylab.service.TransactionService;
import ru.ylab.service.impl.TransactionServiceImpl;

import java.util.List;

public class TransactionHistory implements ItemAction {
    private TransactionService transactionService = TransactionServiceImpl.getInstance();

    @Override
    public void execution() {
        List<Transaction> transactionList = transactionService.findAll();
        if (transactionList.isEmpty()) {
            System.out.println("Transactions not found.");
        } else {
            transactionList.forEach(System.out::println);
        }
    }
}
