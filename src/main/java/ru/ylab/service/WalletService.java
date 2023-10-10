package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Wallet;

public interface WalletService {
    Wallet add(Long userId, Wallet wallet) throws NotFoundException;

    Wallet findById(Long walletId) throws NotFoundException;

    Wallet putMoney(Long walletId, String moneyValue) throws NotFoundException;

    Wallet getMoney(Long walletId, String moneyValue) throws NotFoundException;
}
