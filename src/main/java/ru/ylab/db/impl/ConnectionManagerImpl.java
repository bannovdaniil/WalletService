package ru.ylab.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.DataBaseDriverLoadException;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * {@inheritDoc}
 */
@Component
@RequiredArgsConstructor
public final class ConnectionManagerImpl implements ConnectionManager {
    private final PropertiesUtil propertiesUtil;

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
        loadDriver(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.DRIVER_CLASS_KEY));
        Connection connection = DriverManager.getConnection(
                propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.URL_KEY),
                propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY),
                propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY)
        );
        connection.setSchema(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.DEFAULT_SCHEMA_NAME));
        return connection;
    }

}
