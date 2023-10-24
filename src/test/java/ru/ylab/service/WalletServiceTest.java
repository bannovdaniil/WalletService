package ru.ylab.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Session;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.BalanceType;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.repository.SessionRepository;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.SessionRepositoryImpl;
import ru.ylab.repository.impl.TransactionRepositoryImpl;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.impl.WalletServiceImpl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

class WalletServiceTest {
    private static WalletService walletService;
    private static UserRepository mockUserRepository;
    private static WalletRepository mockWalletRepository;
    private static SessionRepository mockSessionRepository;
    private static TransactionRepository mockTransactionRepository;

    private static void setMock(UserRepository mock) {
        try {
            Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(WalletRepository mock) {
        try {
            Field instance = WalletRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(SessionRepository mock) {
        try {
            Field instance = SessionRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
    static void beforeAll() throws NoSuchFieldException, IllegalAccessException {
        mockUserRepository = Mockito.mock(UserRepository.class);
        setMock(mockUserRepository);
        mockWalletRepository = Mockito.mock(WalletRepository.class);
        setMock(mockWalletRepository);
        mockSessionRepository = Mockito.mock(SessionRepository.class);
        setMock(mockSessionRepository);
        mockTransactionRepository = Mockito.mock(TransactionRepository.class);
        setMock(mockTransactionRepository);

        walletService = WalletServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);

        instance = WalletRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);

        instance = WalletServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);

        instance = SessionRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);

        instance = TransactionRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);

    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockUserRepository);
        Mockito.reset(mockWalletRepository);
    }

    @Test
    void find() throws NotFoundException {
        Long expectedId = 1L;

        Wallet wallet = new Wallet(
                expectedId,
                "wallet",
                BigDecimal.ZERO
        );

        Mockito.doReturn(Optional.of(wallet)).when(mockWalletRepository).findById(Mockito.anyLong());

        Wallet result = walletService.findById(expectedId);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void findByIdNotFound() {
        Mockito.doReturn(false).when(mockWalletRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> walletService.findById(1L), "Not found."
        );
        Assertions.assertEquals("Wallet not found", exception.getMessage());
    }

    @Test
    void changeBalanceGet() throws NotFoundException {
        Session session = new Session(
                UUID.randomUUID(),
                LocalDateTime.now(),
                1L
        );
        Wallet wallet = new Wallet(
                1L,
                "wallet",
                new BigDecimal(1000)
        );
        User user = new User(
                1L,
                "f1",
                "L1",
                "123",
                wallet
        );
        Mockito.doReturn(Optional.of(session)).when(mockSessionRepository).findById(Mockito.any());
        Mockito.doReturn(Optional.of(user)).when(mockUserRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.of(wallet)).when(mockWalletRepository).findById(Mockito.anyLong());

        WalletIncomingDto dto = new WalletIncomingDto(
                BalanceType.GET,
                "4.31"
        );

        walletService.changeBalance(UUID.randomUUID(), dto);

        Mockito.verify(mockWalletRepository).update(Mockito.any());
    }

    @Test
    void changeBalancePut() throws NotFoundException {
        Session session = new Session(
                UUID.randomUUID(),
                LocalDateTime.now(),
                1L
        );
        Wallet wallet = new Wallet(
                1L,
                "wallet",
                new BigDecimal(1000)
        );
        User user = new User(
                1L,
                "f1",
                "L1",
                "123",
                wallet
        );
        Mockito.doReturn(Optional.of(session)).when(mockSessionRepository).findById(Mockito.any());
        Mockito.doReturn(Optional.of(user)).when(mockUserRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.of(wallet)).when(mockWalletRepository).findById(Mockito.anyLong());

        WalletIncomingDto dto = new WalletIncomingDto(
                BalanceType.PUT,
                "4.31"
        );

        walletService.changeBalance(UUID.randomUUID(), dto);

        Mockito.verify(mockWalletRepository).update(Mockito.any());
    }

}