package ru.ylab.model;

import java.math.BigDecimal;

/**
 * Счет пользователя.
 * - название счета
 * - баланс счета.
 */
public class Wallet {
    private final Long id;
    private String name;
    private BigDecimal balance;

    public Wallet(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
