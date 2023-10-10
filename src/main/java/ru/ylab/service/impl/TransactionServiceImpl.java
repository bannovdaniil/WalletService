package ru.ylab.service.impl;

import ru.ylab.model.Transaction;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.impl.TransactionRepositoryImpl;
import ru.ylab.service.TransactionService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private static TransactionService instance;

    private TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    private TransactionServiceImpl() {
    }

    public static synchronized TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
