package ru.ylab.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.impl.WalletServiceImpl;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

class WalletServiceTest {
    private static WalletService walletService;
    private static UserRepository mockUserRepository;
    private static WalletRepository mockWalletRepository;
    private static UserRepositoryImpl oldUserRepositoryImplInstance;
    private static WalletRepositoryImpl oldWalletRepositoryInstance;
    private static WalletServiceImpl oldWalletServiceInstance;
    private static PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();

    private static void setMock(UserRepository mock) {
        try {
            Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldUserRepositoryImplInstance = (UserRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(WalletRepository mock) {
        try {
            Field instance = WalletRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldWalletRepositoryInstance = (WalletRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException, IllegalAccessException {
        mockUserRepository = Mockito.mock(UserRepository.class);
        setMock(mockUserRepository);
        mockWalletRepository = Mockito.mock(WalletRepository.class);
        setMock(mockWalletRepository);

        Field instance = WalletServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        oldWalletServiceInstance = (WalletServiceImpl) instance.get(instance);

        walletService = WalletServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldUserRepositoryImplInstance);

        instance = WalletRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldWalletRepositoryInstance);

        instance = WalletServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldWalletServiceInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockUserRepository);
        Mockito.reset(mockWalletRepository);
    }

    @Test
    void add() throws NotFoundException {
        Long expectedId = 1L;

        Wallet wallet = new Wallet(
                expectedId,
                null,
                "wallet",
                BigDecimal.ZERO
        );

        User user = new User(1L,
                "f1 name",
                "l1 name",
                passwordEncoder.encode("password"),
                null
        );

        Mockito.doReturn(wallet).when(mockWalletRepository).save(Mockito.any(Wallet.class));
        Mockito.doReturn(Optional.of(user)).when(mockUserRepository).findById(Mockito.anyLong());

        Wallet result = walletService.add(1L, wallet);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void find() throws NotFoundException {
        Long expectedId = 1L;

        Wallet wallet = new Wallet(
                expectedId,
                null,
                "wallet",
                BigDecimal.ZERO
        );

        Mockito.doReturn(Optional.of(wallet)).when(mockWalletRepository).findById(Mockito.anyLong());

        Wallet result = walletService.findById(expectedId);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void findByIdNotFound() {
        Optional<User> user = Optional.empty();

        Mockito.doReturn(false).when(mockWalletRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    walletService.findById(1L);
                }, "Not found."
        );
        Assertions.assertEquals("Wallet not found", exception.getMessage());
    }

}