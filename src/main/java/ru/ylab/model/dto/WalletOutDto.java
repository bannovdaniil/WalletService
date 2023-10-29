package ru.ylab.model.dto;

import java.math.BigDecimal;

public class WalletOutDto {
    private BigDecimal balance;

    public WalletOutDto(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
