package ru.ylab.service.impl;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.WalletService;

public class WalletServiceImpl implements WalletService {
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();

    @Override
    public Wallet add(Long userId, Wallet wallet) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User for wallet isn't correct.")
        );
        wallet.setOwner(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet find(Long walletId) throws NotFoundException {
        return walletRepository.findById(walletId).orElseThrow(
                () -> new NotFoundException("Wallet not found")
        );
    }

    @Override
    public void update(Wallet wallet) throws NotFoundException {
        walletRepository.update(wallet);
    }

    @Override
    public void delete(Long walletId) throws NotFoundException {
        userRepository.deleteById(walletId);
    }
}
