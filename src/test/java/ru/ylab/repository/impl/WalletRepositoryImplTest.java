package ru.ylab.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.Constants;
import ru.ylab.TestApplicationConfig;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;

import java.math.BigDecimal;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(classes = TestApplicationConfig.class)
class WalletRepositoryImplTest {

    private static final int containerPort = 5432;
    private static final int localPort = 54321;
    private final PropertiesUtil propertiesUtil;
    private final LiquibaseUtil liquibaseUtil;
    private final WalletRepository walletRepository;
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
        String expectedName = "new Wallet Test Name";
        Wallet wallet = new Wallet(
                null,
                expectedName,
                new BigDecimal("100.12"));

        Long walletId = walletRepository.save(wallet).getId();
        Optional<Wallet> resultWallet = walletRepository.findById(walletId);

        Assertions.assertTrue(resultWallet.isPresent());
        Assertions.assertEquals(expectedName, resultWallet.get().getName());
    }

    @DisplayName("Update Wallet")
    @Test
    void update() throws NotFoundException {
        String expectedName = "UPDATE Wallet Name";
        String expectedBalance = "555.55";

        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        wallet = walletRepository.save(wallet);

        Wallet walletForUpdate = new Wallet(
                wallet.getId(),
                expectedName,
                new BigDecimal(expectedBalance)
        );

        walletRepository.update(walletForUpdate);

        Wallet walletResult = walletRepository.findById(wallet.getId()).orElseThrow();

        Assertions.assertNotEquals(expectedName, wallet.getName());
        Assertions.assertEquals(expectedName, walletResult.getName());
        Assertions.assertEquals(expectedBalance, walletResult.getBalance().toString());
    }

    @DisplayName("Find Wallet by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        walletRepository.save(wallet);

        Optional<Wallet> walletResult = walletRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, walletResult.isPresent());
        walletResult.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @DisplayName("Find all Wallet")
    @Test
    void findAll() {
        int expectedSize = walletRepository.findAll().size() + 1;

        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        walletRepository.save(wallet);

        int resultSize = walletRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist Wallet by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long expectedId, Boolean expectedValue) {
        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        walletRepository.save(wallet);

        boolean isRoleExist = walletRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }
}