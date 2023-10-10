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
    private LocalDateTime time;
    private TransactionType type;
    private BigDecimal sum;
    private User user;

    public Transaction(LocalDateTime time, TransactionType type, BigDecimal sum, User user) {
        this.time = time;
        this.type = type;
        this.sum = sum;
        this.user = user;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transaction.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("time=" + time)
                .add("type=" + type)
                .add("sum=" + sum)
                .add("user=" + user)
                .toString();
    }
}
