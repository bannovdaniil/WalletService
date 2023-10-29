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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.configuration.TestApplicationConfig;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;

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
class UserRepositoryImplTest {
    private static final int containerPort = 5432;
    private static final int localPort = 54321;
    private final PropertiesUtil propertiesUtil;
    private final LiquibaseUtil liquibaseUtil;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    @Container
    public PostgreSQLContainer<?> container;

    @BeforeAll
    void beforeAll() {
        container = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("wallet_db")
                .withUsername(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY))
                .withPassword(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY))
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
        String expectedName = "new User Test Name";

        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        wallet = walletRepository.save(wallet);

        User user = new User(
                null,
                expectedName,
                "Lastname",
                passwordEncoder.encode("123"),
                wallet
        );

        Long userId = userRepository.save(user).getId();
        Optional<User> resultUser = userRepository.findById(userId);

        Assertions.assertTrue(resultUser.isPresent());
        Assertions.assertEquals(expectedName, resultUser.get().getFirstName());
    }

    @Test
    void update() throws NotFoundException {
        String expectedFirstname = "UPDATE User First";
        String expectedLastname = "UPDATE User Last";
        String expectedPassword = passwordEncoder.encode("new password");

        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        wallet = walletRepository.save(wallet);

        User user = new User(
                null,
                "Firstname",
                "Lastname",
                passwordEncoder.encode("123"),
                wallet
        );

        user = userRepository.save(user);

        User userForUpdate = new User(
                user.getId(),
                expectedFirstname,
                expectedLastname,
                expectedPassword,
                wallet
        );

        userRepository.update(userForUpdate);

        User userResult = userRepository.findById(user.getId()).orElseThrow();

        Assertions.assertNotEquals(expectedFirstname, user.getFirstName());
        Assertions.assertNotEquals(expectedLastname, user.getLastName());
        Assertions.assertNotEquals(expectedPassword, user.getHashPassword());

        Assertions.assertEquals(expectedFirstname, userResult.getFirstName());
        Assertions.assertEquals(expectedLastname, userResult.getLastName());
        Assertions.assertEquals(expectedPassword, userResult.getHashPassword());
    }

    @DisplayName("Find by ID")
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

        wallet = walletRepository.save(wallet);

        User user = new User(
                null,
                "Firstname",
                "Lastname",
                passwordEncoder.encode("123"),
                wallet
        );
        userRepository.save(user);

        Optional<User> userResult = userRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, userResult.isPresent());
        if (userResult.isPresent()) {
            Assertions.assertEquals(expectedId, userResult.get().getId());
        }
    }

    @Test
    void findByWalletId() {
        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        wallet = walletRepository.save(wallet);

        User user = new User(
                null,
                "Firstname",
                "Lastname",
                passwordEncoder.encode("123"),
                wallet
        );
        user = userRepository.save(user);
        Long expectedUserId = user.getId();

        Optional<User> userResult = userRepository.findByWalletId(wallet.getId());

        Assertions.assertTrue(userResult.isPresent());
        Assertions.assertEquals(expectedUserId, userResult.get().getId());
    }

    @Test
    void findAll() {
        int expectedSize = userRepository.findAll().size() + 1;

        Wallet wallet = new Wallet(
                null,
                "NEW WALLET",
                new BigDecimal("123.12"));

        wallet = walletRepository.save(wallet);

        User user = new User(
                null,
                "Firstname",
                "Lastname",
                passwordEncoder.encode("123"),
                wallet
        );

        userRepository.save(user);

        int resultSize = userRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
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

        wallet = walletRepository.save(wallet);

        User user = new User(
                null,
                "Firstname",
                "Lastname",
                passwordEncoder.encode("123"),
                wallet
        );
        userRepository.save(user);

        boolean isRoleExist = userRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }
}