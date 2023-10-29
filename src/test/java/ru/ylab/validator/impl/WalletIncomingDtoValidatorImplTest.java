package ru.ylab.validator.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.ylab.model.dto.BalanceType;
import ru.ylab.model.dto.WalletIncomingDto;

class WalletIncomingDtoValidatorImplTest {

    @ParameterizedTest
    @CsvSource({
            "GET, '10', true",
            "GET, '10.01', true",
            "GET, '10,01', true",
            "PUT, '10', true",
            "GET, '10,001', false",
            "GET, '', false",
            "GET, '-1', false",
            "GE, '10', false",
            "0, '100', false"
    })
    void isValid(String type, String sum, Boolean exceptedValue) {
        WalletIncomingDtoValidatorImpl dtoValidator = new WalletIncomingDtoValidatorImpl();
        WalletIncomingDto dto;
        try {
            dto = new WalletIncomingDto(
                    BalanceType.valueOf(type),
                    sum
            );
        } catch (Throwable ex) {
            dto = null;
        }

        Assertions.assertEquals(exceptedValue, dtoValidator.isValid(dto, null));
    }

}