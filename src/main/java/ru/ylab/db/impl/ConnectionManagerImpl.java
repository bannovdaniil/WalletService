package ru.ylab.db.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ylab.Constants;
import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.DataBaseDriverLoadException;
import ru.ylab.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * {@inheritDoc}
 */
@Component
public final class ConnectionManagerImpl implements ConnectionManager {
    private final PropertiesUtil propertiesUtil;

    @Autowired
    public ConnectionManagerImpl(PropertiesUtil propertiesUtil) {
        this.propertiesUtil = propertiesUtil;
        loadDriver(propertiesUtil.getProperties(Constants.DRIVER_CLASS_KEY));
    }

    /**
     * Подгружаем драйвер
     */
    private void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverLoadException("Database driver not loaded.");
        }
    }

    /**
     * Вернет соединение с базой и установит рабочую схему.
     *
     * @throws SQLException - при любых ошибках с базой.
     */
    @Override
    @SuppressWarnings("squid:S2095")
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                propertiesUtil.getProperties(Constants.URL_KEY, "db"),
                propertiesUtil.getProperties(Constants.USERNAME_KEY, "db"),
                propertiesUtil.getProperties(Constants.PASSWORD_KEY, "db")
        );
        connection.setSchema(propertiesUtil.getProperties(Constants.DEFAULT_SCHEMA_NAME));
        return connection;
    }

}
