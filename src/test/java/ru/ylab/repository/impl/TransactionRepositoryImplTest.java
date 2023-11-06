package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ylab.TestApplicationConfig;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TransactionRepositoryImplTest extends TestApplicationConfig {
    private final TransactionRepository transactionRepository;
    private final PostgreSQLContainer container;

    @BeforeAll
    void beforeAll() {
        container.start();
    }

    @DisplayName("Save Transaction")
    @Test
    void save() {
        String expectedSum = "100.12";
        Transaction transaction = new Transaction(
                LocalDateTime.now(),
                TransactionType.PUT,
                new BigDecimal(expectedSum),
                1L);

        Long transactionId = transactionRepository.save(transaction).getId();
        List<Transaction> transactionList = transactionRepository.findAll();
        Optional<Transaction> resultTransaction = transactionList.stream().filter(a -> transactionId.equals(a.getId())).findFirst();

        Assertions.assertTrue(resultTransaction.isPresent());
        Assertions.assertEquals(expectedSum, resultTransaction.get().getSum().toString());
    }

    @DisplayName("Find All Transaction")
    @Test
    void findAll() {
        int expectedSize = transactionRepository.findAll().size() + 1;

        Transaction transaction = new Transaction(
                LocalDateTime.now(),
                TransactionType.PUT,
                BigDecimal.TEN,
                1L);

        transactionRepository.save(transaction);

        int resultSize = transactionRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }
}