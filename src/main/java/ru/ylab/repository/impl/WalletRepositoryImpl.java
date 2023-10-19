package ru.ylab.repository.impl;

import ru.ylab.db.ConnectionManager;
import ru.ylab.db.impl.ConnectionManagerImpl;
import ru.ylab.exception.NotFoundException;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления Счета пользователя.
 */
public final class WalletRepositoryImpl implements WalletRepository {

    private static final String SAVE_SQL = """
            INSERT INTO wallets (wallet_name, wallet_balance)
            VALUES (?, ?) ;
            """;
    private static final String UPDATE_SQL = """
            UPDATE wallets
            SET wallet_name = ?,
                wallet_balance = ?
            WHERE wallet_id = ?  ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT wallet_id, wallet_name, wallet_balance FROM wallets
            WHERE wallet_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT wallet_id, wallet_name, wallet_balance FROM wallets;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM wallets
                        WHERE wallet_id = ?
                        LIMIT 1);
            """;
    private static WalletRepository instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private WalletRepositoryImpl() {
    }

    public static synchronized WalletRepository getInstance() {
        if (instance == null) {
            instance = new WalletRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Wallet save(Wallet wallet) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, wallet.getName());
            preparedStatement.setBigDecimal(2, wallet.getBalance());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                wallet = createWallet(resultSet);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return wallet;
    }

    @Override
    public void update(Wallet wallet) throws NotFoundException {
        checkExistById(wallet);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, wallet.getName());
            preparedStatement.setBigDecimal(2, wallet.getBalance());
            preparedStatement.setLong(3, wallet.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        Wallet wallet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                wallet = createWallet(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return Optional.ofNullable(wallet);
    }

    @Override
    public List<Wallet> findAll() {
        List<Wallet> walletList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                walletList.add(createWallet(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return walletList;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return isExists;
    }

    private void checkExistById(Wallet wallet) throws NotFoundException {
        if (!exitsById(wallet.getId())) {
            throw new NotFoundException("Wallet not found");
        }
    }

    private Wallet createWallet(ResultSet resultSet) throws SQLException {
        Long walletId = resultSet.getLong("wallet_id");

        return new Wallet(
                walletId,
                resultSet.getString("wallet_name"),
                resultSet.getBigDecimal("wallet_balance")
        );
    }

}
