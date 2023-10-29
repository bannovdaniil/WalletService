package ru.ylab.model.dto;

import ru.ylab.validator.WalletIncomingDtoValidation;

@WalletIncomingDtoValidation
public class WalletIncomingDto {
    private BalanceType type;
    private String sum;

    public WalletIncomingDto() {
    }

    public WalletIncomingDto(BalanceType type, String sum) {
        this.type = type;
        this.sum = sum;
    }

    public BalanceType getType() {
        return type;
    }

    public String getSum() {
        return sum;
    }
}
