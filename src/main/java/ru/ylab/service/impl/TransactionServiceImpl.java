package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.model.Transaction;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.service.TransactionService;

import java.util.List;

/**
 * Бизнес логика Transaction Событий которые происходят со счетом.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
