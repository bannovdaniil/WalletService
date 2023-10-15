package ru.ylab.repository.impl;

import ru.ylab.db.ConnectionManager;
import ru.ylab.db.impl.ConnectionManagerImpl;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления Action
 */
public final class ActionRepositoryImpl implements ActionRepository {
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static ActionRepository instance;

    private ActionRepositoryImpl() {
    }

    public static synchronized ActionRepository getInstance() {
        if (instance == null) {
            instance = new ActionRepositoryImpl();
        }
        return instance;
    }

    private static final String SAVE_SQL = """
            INSERT INTO actions (action_time, action_action, action_information, user_id)
            VALUES (?, ?, ?, ?) ;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT action_id, action_time, action_action, action_information, user_id FROM actions;
            """;

    @Override
    public Action save(Action action) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, action.getTime());
            preparedStatement.setString(2, action.getUserAction());
            preparedStatement.setString(3, action.getInformation());
            preparedStatement.setLong(4, action.getUserId() == null ? 0 : action.getUserId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                action = createAction(resultSet);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return action;
    }

    @Override
    public List<Action> findAll() {
        List<Action> actionList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                actionList.add(createAction(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return actionList;
    }

    private Action createAction(ResultSet resultSet) throws SQLException {
        Long actionId = resultSet.getLong("action_id");
        return new Action(
                actionId,
                resultSet.getObject("action_time", LocalDateTime.class),
                resultSet.getString("action_action"),
                resultSet.getLong("user_id"),
                resultSet.getString("action_information")
        );
    }

}
