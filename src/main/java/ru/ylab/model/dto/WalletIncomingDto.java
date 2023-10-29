package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ylab.validator.WalletIncomingDtoValidation;

@WalletIncomingDtoValidation
@Getter
@AllArgsConstructor
public class WalletIncomingDto {
    private BalanceType type;
    private String sum;
}
