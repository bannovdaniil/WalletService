package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.WalletOutDto;

/**
 * Маппер для Wallet
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {
    WalletOutDto walletToDto(Wallet wallet);
}
