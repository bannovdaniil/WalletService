package ru.ylab.validator.impl;

import ru.ylab.model.dto.BalanceType;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.validator.Validator;

import java.util.Set;

import static ru.ylab.Constants.REGEXP_FORMAT_MONEY;

/**
 * {@inheritDoc}
 */
public class WalletIncomingDtoValidatorImpl implements Validator<WalletIncomingDto> {
    private static Validator<WalletIncomingDto> instance;

    private WalletIncomingDtoValidatorImpl() {
    }

    public static synchronized Validator<WalletIncomingDto> getInstance() {
        if (instance == null) {
            instance = new WalletIncomingDtoValidatorImpl();
        }
        return instance;
    }

    /**
     * Проверка на Null
     * Проверка Enum
     * Проверка Входящего числа.
     */
    @Override
    public boolean isValid(WalletIncomingDto dto) {

        return (dto != null
                && dto.getType() != null
                && Set.of(BalanceType.values()).contains(dto.getType())
                && dto.getSum() != null
                && !dto.getSum().isEmpty()
                && !dto.getSum().isBlank()
                && dto.getSum().matches(REGEXP_FORMAT_MONEY)
        );
    }
}
