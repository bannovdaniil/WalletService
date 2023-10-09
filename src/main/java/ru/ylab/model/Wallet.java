package ru.ylab.model;

import java.math.BigDecimal;

public class Wallet extends Entity {
    private User owner;
    private String name;
    private BigDecimal balance;

    public Wallet(Long id, User owner, String name, BigDecimal balance) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
