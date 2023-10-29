package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.service.impl.TransactionServiceImpl;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository mockTransactionRepository;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        Mockito.reset(mockTransactionRepository);
    }


    @Test
    void findAll() {
        transactionService.findAll();
        Mockito.verify(mockTransactionRepository).findAll();
    }
}