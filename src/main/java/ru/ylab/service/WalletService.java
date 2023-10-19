package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;

public interface WalletService {
    /**
     * Найти счет по его ID
     *
     * @param walletId
     * @return
     * @throws NotFoundException
     */
    Wallet findById(Long walletId) throws NotFoundException;

    /**
     * Внести деньги на счет
     *
     * @param user       - владелец счета
     * @param moneyValue - сумма
     */
    Wallet putMoney(User user, String moneyValue) throws NotFoundException;

    /**
     * Снять деньги со счета
     *
     * @param user       - владелец счета
     * @param moneyValue - сумма
     */
    Wallet getMoney(User user, String moneyValue) throws NotFoundException;
}
