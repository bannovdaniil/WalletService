package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class BalanceDto {
    private BigDecimal balance;
}
