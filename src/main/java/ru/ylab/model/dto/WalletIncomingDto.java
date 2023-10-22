package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WalletIncomingDto {
    private BalanceType type;
    private String sum;
}
