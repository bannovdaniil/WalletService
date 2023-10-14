package ru.ylab.db.impl;

import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.DataBaseDriverLoadException;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.DbPropertiesUtilImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManagerImpl implements ConnectionManager {
    private static PropertiesUtil propertiesUtil;
    private static final String DRIVER_CLASS_KEY = "db.driver-class-name";
    private static final String DATABASE_SCHEMA = "db.schema";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static ConnectionManager instance;

    private ConnectionManagerImpl() {
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            propertiesUtil = DbPropertiesUtilImpl.getInstance();
            loadDriver(propertiesUtil.getProperties(DRIVER_CLASS_KEY), propertiesUtil.getProperties(DATABASE_SCHEMA));
        }
        return instance;
    }

    private static void loadDriver(String driverClass, String databaseSchema) {
        DbPropertiesUtilImpl.getInstance();
        try {
            Class.forName(driverClass + "&currentSchema=" + databaseSchema);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverLoadException("Database driver not loaded.");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                propertiesUtil.getProperties(URL_KEY),
                propertiesUtil.getProperties(USERNAME_KEY),
                propertiesUtil.getProperties(PASSWORD_KEY)
        );
    }

}
