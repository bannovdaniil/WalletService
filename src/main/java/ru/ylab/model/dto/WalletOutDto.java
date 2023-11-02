package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Исходящее DTO для счета.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WalletOutDto {
    private BigDecimal balance;
}
