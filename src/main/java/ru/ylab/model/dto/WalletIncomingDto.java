package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.ylab.validator.WalletIncomingDtoValidation;

/**
 * Входящее DTO для счета.
 * Тип операции
 * Сумма операции.
 */
@WalletIncomingDtoValidation
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WalletIncomingDto {
    private BalanceType type;
    private String sum;
}
