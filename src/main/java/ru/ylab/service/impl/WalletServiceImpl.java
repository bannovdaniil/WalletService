package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.exception.NotFoundException;
import ru.ylab.mapper.WalletMapper;
import ru.ylab.model.*;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.model.dto.WalletOutDto;
import ru.ylab.repository.SessionRepository;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.service.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.ylab.Constants.REGEXP_FORMAT_MONEY;

/**
 * Бизнес логика работы со счетами пользователя.
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Wallet findById(Long walletId) throws NotFoundException {
        return walletRepository.findById(walletId).orElseThrow(
                () -> new NotFoundException("Wallet not found")
        );
    }

    /**
     * Внести деньги на счет
     *
     * @param user       - владелец счета
     * @param moneyValue - сумма
     */
    private void putMoney(User user, String moneyValue) throws NotFoundException {
        if (moneyValue.matches(REGEXP_FORMAT_MONEY) && user != null && user.getWallet() != null) {
            BigDecimal addValue = new BigDecimal(moneyValue.replace(",", "."));

            Long walletId = user.getWallet().getId();
            Wallet wallet = walletRepository.findById(walletId).orElseThrow(
                    () -> new NotFoundException("Wallet Not found.")
            );
            if (addValue.compareTo(BigDecimal.ZERO) > 0) {
                wallet.setBalance(wallet.getBalance().add(addValue));
                walletRepository.update(wallet);

                transactionRepository.save(new Transaction(
                        LocalDateTime.now(),
                        TransactionType.PUT,
                        addValue,
                        user.getId()));
            }
        } else {
            throw new IllegalArgumentException("Bad arguments");
        }
    }

    /**
     * Снять деньги со счета
     *
     * @param user       - владелец счета
     * @param moneyValue - сумма
     */
    private void getMoney(User user, String moneyValue) throws NotFoundException {
        if (moneyValue.matches(REGEXP_FORMAT_MONEY) && user != null && user.getWallet() != null) {
            BigDecimal subtractValue = new BigDecimal(moneyValue.replace(",", "."));

            Long walletId = user.getWallet().getId();
            Wallet wallet = walletRepository.findById(walletId).orElseThrow(
                    () -> new IllegalStateException("Wallet Not found.")
            );

            if (wallet.getBalance().compareTo(subtractValue) < 0) {
                throw new IllegalArgumentException("Not have same money.");
            }

            if (subtractValue.compareTo(BigDecimal.ZERO) > 0) {
                wallet.setBalance(wallet.getBalance().subtract(subtractValue));
                walletRepository.update(wallet);

                transactionRepository.save(new Transaction(
                        LocalDateTime.now(),
                        TransactionType.GET,
                        subtractValue,
                        user.getId()));
            }
        } else {
            throw new IllegalArgumentException("Bad arguments");
        }
    }

    @Override
    public WalletOutDto getBalance(UUID sessionId) {
        User user = getUserFromSession(sessionId);
        Wallet wallet = walletRepository.findById(user.getWallet().getId()).orElseThrow();
        return walletMapper.walletToDto(wallet);
    }

    @Override
    public WalletOutDto changeBalance(UUID sessionId, WalletIncomingDto dto) throws NotFoundException {
        User user = getUserFromSession(sessionId);
        switch (dto.getType()) {
            case GET:
                getMoney(user, dto.getSum());
                break;
            case PUT:
                putMoney(user, dto.getSum());
                break;
            default:
                throw new IllegalArgumentException("Do not allow this method.");
        }

        return new WalletOutDto(
                walletRepository.findById(user.getWallet().getId()).orElseThrow().getBalance()
        );
    }

    private User getUserFromSession(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        return userRepository.findById(session.getUserId()).orElseThrow();
    }
}
