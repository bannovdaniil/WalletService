package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Исходящее DTO для счета.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletOutDto {
    private BigDecimal balance;
}
