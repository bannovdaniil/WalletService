package ru.ylab.service.impl;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.TransactionRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletServiceImpl implements WalletService {
    private static final String REGEXP_FORMAT_MONEY = "^\\d*([\\.,]\\d{1,2})?$";
    private static WalletService instance;
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    private WalletServiceImpl() {
    }

    public static synchronized WalletService getInstance() {
        if (instance == null) {
            instance = new WalletServiceImpl();
        }
        return instance;
    }

    @Override
    public Wallet findById(Long walletId) throws NotFoundException {
        return walletRepository.findById(walletId).orElseThrow(
                () -> new NotFoundException("Wallet not found")
        );
    }

    @Override
    public Wallet putMoney(User user, String moneyValue) throws NotFoundException {
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
                return walletRepository.findById(walletId).orElseThrow();
            }
            return wallet;
        }
        throw new IllegalArgumentException("Bad arguments");
    }

    @Override
    public Wallet getMoney(User user, String moneyValue) throws NotFoundException {
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
                return walletRepository.findById(walletId).orElseThrow();
            }
            return wallet;
        }
        throw new IllegalArgumentException("Bad arguments");
    }

}
