package ru.ylab.repository.impl;

import ru.ylab.model.Transaction;
import ru.ylab.repository.TransactionRepository;

public final class TransactionRepositoryImpl extends RepositoryImpl<Transaction> implements TransactionRepository {
    private static TransactionRepository instance;

    public static synchronized TransactionRepository getInstance() {
        if (instance == null) {
            instance = new TransactionRepositoryImpl();
        }
        return instance;
    }

}
