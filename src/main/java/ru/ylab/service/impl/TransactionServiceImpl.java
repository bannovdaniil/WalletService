package ru.ylab.service.impl;

import ru.ylab.exception.NotFoundException;
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
    public Transaction find(Long id) throws NotFoundException {
        return transactionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Transaction not found.")
        );
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
