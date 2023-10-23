package ru.ylab.model.dto;

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
