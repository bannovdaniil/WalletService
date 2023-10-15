package ru.ylab.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;
import ru.ylab.util.impl.LiquibaseUtilImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
class TransactionRepositoryImplTest {

    private static final int containerPort = 5432;
    private static final int localPort = 54320;
    private static PropertiesUtil propertiesUtil = ApplicationPropertiesUtilImpl.getInstance();
    private static LiquibaseUtil liquibaseUtil = LiquibaseUtilImpl.getInstance();

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("wallet_db")
            .withUsername(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY))
            .withPassword(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY))
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ));
    public static TransactionRepository transactionRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        transactionRepository = TransactionRepositoryImpl.getInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
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
        Optional<Transaction> resultTransaction = transactionList.stream().filter(a -> a.getId() == transactionId).findFirst();

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