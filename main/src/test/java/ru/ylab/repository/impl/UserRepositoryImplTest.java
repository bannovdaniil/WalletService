package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ylab.TestApplicationConfig;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryImplTest extends TestApplicationConfig {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostgreSQLContainer container;

    @BeforeAll
    void beforeAll() {
        container.start();
    }

    @DisplayName("Save User Entity")
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

    @DisplayName("Update User Entity")
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

    @DisplayName("Find User by ID")
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
        userResult.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @DisplayName("find User By Wallet Id")
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

    @DisplayName("find All Users")
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

    @DisplayName("Exist User by ID")
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