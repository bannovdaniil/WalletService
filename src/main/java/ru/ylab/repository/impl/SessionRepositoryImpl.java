package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.Session;
import ru.ylab.repository.SessionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для управления сессиями пользователей.
 */
@Repository
@RequiredArgsConstructor
public final class SessionRepositoryImpl implements SessionRepository {
    private static final String SAVE_SQL = """
            INSERT INTO sessions (session_time, user_id)
            VALUES (?, ?) ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT session_id, session_time, user_id FROM sessions
            WHERE session_id = ?;
            """;
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM sessions WHERE session_id = ?;
            """;

    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM sessions
                        WHERE session_id = ?
                        LIMIT 1);
            """;
    private final ConnectionManager connectionManager;


    @Override
    public Session save(Session session) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, session.getTime());
            preparedStatement.setLong(2, session.getUserId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                session = createSession(resultSet);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return session;
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {
        Session session = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setObject(1, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                session = createSession(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return Optional.ofNullable(session);
    }

    @Override
    public void deleteById(UUID sessionId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL);) {

            preparedStatement.setObject(1, sessionId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public boolean isActive(UUID sessionId) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)) {

            preparedStatement.setObject(1, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return isExists;
    }

    private Session createSession(ResultSet resultSet) throws SQLException {
        return new Session(
                resultSet.getObject("session_id", java.util.UUID.class),
                resultSet.getObject("session_time", LocalDateTime.class),
                resultSet.getLong("user_id")
        );
    }
}
