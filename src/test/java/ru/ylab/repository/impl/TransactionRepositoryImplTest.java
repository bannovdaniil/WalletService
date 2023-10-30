package ru.ylab.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.Constants;
import ru.ylab.TestApplicationConfig;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(classes = TestApplicationConfig.class)
class TransactionRepositoryImplTest {
    private static final int containerPort = 5432;
    private static final int localPort = 54321;
    private final PropertiesUtil propertiesUtil;
    private final LiquibaseUtil liquibaseUtil;
    private final TransactionRepository transactionRepository;
    @Container
    public PostgreSQLContainer<?> container;

    @BeforeAll
    void beforeAll() {
        container = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("wallet_db")
                .withUsername(propertiesUtil.getProperties(Constants.USERNAME_KEY, "test"))
                .withPassword(propertiesUtil.getProperties(Constants.PASSWORD_KEY, "test"))
                .withExposedPorts(containerPort)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
                ));
        System.setProperty("DATASOURCE_URL", "jdbc:postgresql://localhost:" + localPort + "/wallet_db");
        container.start();
    }

    @AfterAll
    void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        liquibaseUtil.init();
    }

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