package ru.ylab.service;

import ru.ylab.model.Transaction;

import java.util.List;

public interface TransactionService {
    /**
     * Вернуть список событий.
     */
    List<Transaction> findAll();
}
