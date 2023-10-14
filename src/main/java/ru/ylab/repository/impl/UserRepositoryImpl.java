package ru.ylab.repository.impl;

import ru.ylab.db.ConnectionManager;
import ru.ylab.db.impl.ConnectionManagerImpl;
import ru.ylab.exception.NotFoundException;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UserRepositoryImpl extends RepositoryImpl<User> implements UserRepository {
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();
    private static UserRepository instance;

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

    private static final String SAVE_SQL = """
            INSERT INTO users (user_firstname, user_lastname, user_password)
            VALUES (?, ? ,?) ;
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET user_firstname = ?,
                user_lastname = ?,
                user_password =?
            WHERE user_id = ?  ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT user_id, user_firstname, user_lastname, user_password FROM users
            WHERE user_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT user_id, user_firstname, user_lastname, user_password FROM users;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM users
                        WHERE user_id = ?
                        LIMIT 1);
            """;

    @Override
    public User save(User user) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getHashPassword());

            if (user.getWallet() != null) {
                walletRepository.save(user.getWallet());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return user;
    }

    @Override
    public void update(User user) throws NotFoundException {
        checkExistById(user);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getHashPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = createUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return userList;
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

    private void checkExistById(User user) throws NotFoundException {
        if (!exitsById(user.getId())) {
            throw new NotFoundException("User not found");
        }
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        Long userId = resultSet.getLong("user_id");
        Wallet wallet = walletRepository.findByUserId(userId);
        return new User(
                userId,
                resultSet.getString("user_firstname"),
                resultSet.getString("user_lastname"),
                resultSet.getString("user_password"),
                wallet
        );
    }

}
