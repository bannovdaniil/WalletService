package ru.ylab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.ylab.util.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность хранящая историю транзакций.
 * - время.
 * - тип транзакции
 * - сумма.
 * - пользователь.
 */
public class Transaction {
    private final Long id;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private final LocalDateTime time;
    private final TransactionType type;
    private final BigDecimal sum;
    private final Long userId;

    public Transaction(LocalDateTime time, TransactionType type, BigDecimal sum, Long userId) {
        this.id = null;
        this.time = time;
        this.type = type;
        this.sum = sum;
        this.userId = userId;
    }

    public Transaction(Long id, LocalDateTime time, TransactionType type, BigDecimal sum, Long userId) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.sum = sum;
        this.userId = userId;
    }

    public Long getId() {
        return id;
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
}
