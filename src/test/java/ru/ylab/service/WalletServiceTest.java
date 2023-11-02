package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.ylab.exception.NotFoundException;
import ru.ylab.mapper.WalletMapper;
import ru.ylab.model.Session;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.BalanceType;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.model.dto.WalletOutDto;
import ru.ylab.repository.SessionRepository;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.service.impl.WalletServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WalletServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private WalletRepository mockWalletRepository;
    @Mock
    private WalletMapper mockWalletMapper;
    @Mock
    private SessionRepository mockSessionRepository;
    @Mock
    private TransactionRepository mockTransactionRepository;
    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        Mockito.reset(mockUserRepository);
        Mockito.reset(mockWalletRepository);
    }

    @DisplayName("Get Wallet by ID")
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

    @DisplayName("Get Wallet by ID - Not found exception")
    @Test
    void findByIdNotFound() {
        Mockito.doReturn(false).when(mockWalletRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> walletService.findById(1L), "Not found."
        );
        Assertions.assertEquals("Wallet not found", exception.getMessage());
    }

    @DisplayName("Change Wallet balance - GET")
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

        WalletOutDto outDto = new WalletOutDto(
                new BigDecimal(dto.getSum())
        );
        Mockito.doReturn(outDto).when(mockWalletMapper).walletToDto(Mockito.any(Wallet.class));

        walletService.changeBalance(UUID.randomUUID(), dto);

        Mockito.verify(mockWalletRepository).update(Mockito.any());
    }

    @DisplayName("Change Wallet balance - PUT")
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
        WalletOutDto outDto = new WalletOutDto(
                new BigDecimal(dto.getSum())
        );
        Mockito.doReturn(outDto).when(mockWalletMapper).walletToDto(Mockito.any(Wallet.class));

        walletService.changeBalance(UUID.randomUUID(), dto);

        Mockito.verify(mockWalletRepository).update(Mockito.any());
    }

}