package ru.ylab.service.impl;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.TransactionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.TransactionRepositoryImpl;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletServiceImpl implements WalletService {
    private static WalletService instance;
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();
    private String regexFormatMoney = "^\\d*([\\.,]\\d{1,2})?$";

    private WalletServiceImpl() {
    }

    public static synchronized WalletService getInstance() {
        if (instance == null) {
            instance = new WalletServiceImpl();
        }
        return instance;
    }

    @Override
    public Wallet add(Long userId, Wallet wallet) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User for wallet isn't correct.")
        );
        wallet.setOwner(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findById(Long walletId) throws NotFoundException {
        return walletRepository.findById(walletId).orElseThrow(
                () -> new NotFoundException("Wallet not found")
        );
    }

    @Override
    public Wallet putMoney(Long walletId, String moneyValue) throws NotFoundException {
        if (moneyValue.matches(regexFormatMoney)) {
            BigDecimal addValue = new BigDecimal(moneyValue.replace(",", "."));

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
                        wallet.getOwner()));
                return walletRepository.findById(walletId).orElseThrow();
            }
            return wallet;
        }
        throw new IllegalArgumentException("Bad arguments");
    }

    @Override
    public Wallet getMoney(Long walletId, String moneyValue) throws NotFoundException {
        if (moneyValue.matches(regexFormatMoney)) {
            BigDecimal subtractValue = new BigDecimal(moneyValue.replace(",", "."));

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
                        wallet.getOwner()));
                return walletRepository.findById(walletId).orElseThrow();
            }
            return wallet;
        }
        throw new IllegalArgumentException("Bad arguments");
    }

}
