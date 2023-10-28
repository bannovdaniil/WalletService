package ru.ylab.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.impl.TransactionRepositoryImpl;
import ru.ylab.service.impl.TransactionServiceImpl;

import java.lang.reflect.Field;

class TransactionServiceTest {
    private static TransactionRepository mockTransactionRepository;
    private static TransactionService transactionService;

    private static void setMock(TransactionRepository mock) {
        try {
            Field instance = TransactionRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockTransactionRepository = Mockito.mock(TransactionRepository.class);
        setMock(mockTransactionRepository);
        transactionService = TransactionServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = TransactionRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

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