package ru.ylab.db.impl;

import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.DataBaseDriverLoadException;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManagerImpl implements ConnectionManager {
    private static PropertiesUtil propertiesUtil;
    private static ConnectionManager instance;

    private ConnectionManagerImpl() {
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            propertiesUtil = ApplicationPropertiesUtilImpl.getInstance();
            loadDriver(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.DRIVER_CLASS_KEY));
        }
        return instance;
    }

    private static void loadDriver(String driverClass) {
        ApplicationPropertiesUtilImpl.getInstance();
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverLoadException("Database driver not loaded.");
        }
    }

    @Override
    @SuppressWarnings("squid:S2095")
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.URL_KEY),
                propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY),
                propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY)
        );
        connection.setSchema(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.DEFAULT_SCHEMA_NAME));
        return connection;
    }

}
