package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Wallet;

public interface WalletService {
    Wallet add(Long userId, Wallet wallet) throws NotFoundException;

    Wallet find(Long walletId) throws NotFoundException;

    void update(Wallet wallet) throws NotFoundException;

    void delete(Long walletId) throws NotFoundException;
}