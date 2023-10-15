package ru.ylab.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Wallet extends Entity {
    private String name;
    private BigDecimal balance;

    public Wallet(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
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

    @Override
    public String toString() {
        return "Wallet{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", balance=" + NumberFormat.getCurrencyInstance().format(balance) +
               '}';
    }
}
