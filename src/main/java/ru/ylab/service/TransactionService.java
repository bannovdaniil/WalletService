package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction find(Long id) throws NotFoundException;

    List<Transaction> findAll();
}
