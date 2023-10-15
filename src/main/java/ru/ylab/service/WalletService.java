package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;

public interface WalletService {
    Wallet findById(Long walletId) throws NotFoundException;

    Wallet putMoney(User user, String moneyValue) throws NotFoundException;

    Wallet getMoney(User user, String moneyValue) throws NotFoundException;
}
