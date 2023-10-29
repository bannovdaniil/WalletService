package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.ylab.validator.WalletIncomingDtoValidation;

@WalletIncomingDtoValidation
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WalletIncomingDto {
    private BalanceType type;
    private String sum;
}
