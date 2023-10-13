package ru.ylab.service;

import ru.ylab.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findAll();
}
