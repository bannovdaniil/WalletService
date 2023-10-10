package ru.ylab.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;

public class Transaction extends Entity {
    private LocalDateTime time;
    private TransactionType type;
    private BigDecimal sum;

    public Transaction(LocalDateTime time, TransactionType type, BigDecimal sum) {
        this.time = time;
        this.type = type;
        this.sum = sum;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "Transaction{" +
               "id=" + id +
               ", time=" + time +
               ", type=" + type +
               ", sum=" + NumberFormat.getCurrencyInstance().format(sum) +
               '}';
    }
}
