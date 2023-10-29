package ru.ylab.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ylab.model.dto.BalanceType;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.validator.WalletIncomingDtoValidation;

import java.util.Set;

import static ru.ylab.Constants.REGEXP_FORMAT_MONEY;

public class WalletIncomingDtoValidatorImpl implements ConstraintValidator<WalletIncomingDtoValidation, WalletIncomingDto> {

    /**
     * Проверка на Null
     * Проверка Enum
     * Проверка Входящего числа.
     */
    @Override
    public boolean isValid(WalletIncomingDto dto, ConstraintValidatorContext context) {
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
