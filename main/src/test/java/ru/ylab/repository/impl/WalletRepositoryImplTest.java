package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ylab.TestApplicationConfig;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class WalletRepositoryImplTest extends TestApplicationConfig {
    private final WalletRepository walletRepository;
    private final PostgreSQLContainer container;

    @BeforeAll
    void beforeAll() {
        container.start();
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