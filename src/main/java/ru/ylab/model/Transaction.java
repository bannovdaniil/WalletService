package ru.ylab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import ru.ylab.Constants;

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
@Getter
public class Transaction extends Entity {
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
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
