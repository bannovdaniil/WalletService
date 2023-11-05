package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.repository.TransactionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления Transaction
 */
@Component
@RequiredArgsConstructor
public final class TransactionRepositoryImpl implements TransactionRepository {
    private static final String SAVE_SQL = """
            INSERT INTO transactions (transaction_time, transaction_type, transaction_sum, user_id)
            VALUES (?, ?, ?, ?) ;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT transaction_id, transaction_time, transaction_type, transaction_sum, user_id FROM transactions;
            """;
    private final ConnectionManager connectionManager;

    @Override
    public Transaction save(Transaction transaction) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, transaction.getTime());
            preparedStatement.setString(2, transaction.getType().toString());
            preparedStatement.setBigDecimal(3, transaction.getSum());
            preparedStatement.setLong(4, transaction.getUserId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                transaction = createTransaction(resultSet);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactionList.add(createTransaction(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return transactionList;
    }

    private Transaction createTransaction(ResultSet resultSet) throws SQLException {
        Long transactionId = resultSet.getLong("transaction_id");
        return new Transaction(
                transactionId,
                resultSet.getObject("transaction_time", LocalDateTime.class),
                TransactionType.valueOf(resultSet.getString("transaction_type")),
                resultSet.getBigDecimal("transaction_sum"),
                resultSet.getLong("user_id")
        );
    }

}
