package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.model.dto.WalletOutDto;

import java.util.UUID;

public interface WalletService {
    /**
     * Найти счет по его ID
     */
    Wallet findById(Long walletId) throws NotFoundException;

    /**
     * Получить баланс счета авторизированного пользователя.
     */
    WalletOutDto getBalance(UUID sessionId);

    /**
     * Внести или Снеть деньги со счета
     *
     * @param sessionId - сессия владельца счета
     * @param dto       - WalletIncomingDto сумма и тип операции
     */
    WalletOutDto changeBalance(UUID sessionId, WalletIncomingDto dto) throws NotFoundException;
}
