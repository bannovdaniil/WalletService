package ru.ylab.customloggingstarter.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.ylab.customloggingstarter.db.ConnectionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor
public final class ConnectionManagerImpl implements ConnectionManager {
    private final DataSource dataSource;
    @Value("${spring.liquibase.default-schema}")
    private String defaultSchema;

    /**
     * Вернет соединение с базой и установит рабочую схему.
     *
     * @throws SQLException - при любых ошибках с базой.
     */
    @Override
    @SuppressWarnings("squid:S2095")
    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setSchema(defaultSchema);

        return connection;
    }

}
