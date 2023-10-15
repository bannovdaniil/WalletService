package ru.ylab.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * Сущность хранящая историю транзакций.
 * - время.
 * - тип транзакции
 * - сумма.
 * - пользователь.
 */
public class Transaction extends Entity {
    private final LocalDateTime time;
    private final TransactionType type;
    private final BigDecimal sum;
    private final Long userId;

    public Transaction(LocalDateTime time, TransactionType type, BigDecimal sum, Long userId) {
        this.time = time;
        this.type = type;
        this.sum = sum;
        this.userId = userId;
    }

    public Transaction(Long id, LocalDateTime time, TransactionType type, BigDecimal sum, Long userId) {
        super.setId(id);
        this.time = time;
        this.type = type;
        this.sum = sum;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transaction.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("time=" + time)
                .add("type=" + type)
                .add("sum=" + sum)
                .add("userId=" + userId)
                .toString();
    }
}
