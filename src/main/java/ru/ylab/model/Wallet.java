package ru.ylab.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Счет пользователя.
 * - название счета
 * - баланс счета.
 */
@Setter
@Getter
public class Wallet {
    private final Long id;
    @Setter(AccessLevel.NONE)
    private String name;
    private BigDecimal balance;

    public Wallet(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", balance=" + NumberFormat.getCurrencyInstance().format(balance) +
               '}';
    }
}
