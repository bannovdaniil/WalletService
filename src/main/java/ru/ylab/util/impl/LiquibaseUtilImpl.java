package ru.ylab.util.impl;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.ylab.db.ConnectionManager;
import ru.ylab.db.impl.ConnectionManagerImpl;
import ru.ylab.exception.DatabaseConnectionException;
import ru.ylab.exception.LiquibaseProcessException;
import ru.ylab.util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@inheritDoc}
 */
public class LiquibaseUtilImpl implements LiquibaseUtil {
    private static LiquibaseUtil instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private LiquibaseUtilImpl() {
    }

    public static synchronized LiquibaseUtil getInstance() {
        if (instance == null) {
            instance = new LiquibaseUtilImpl();
        }
        return instance;
    }

    @Override
    @SuppressWarnings("squid:S1874")
    public void init() {
        try {
            Connection connection = connectionManager.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);

            liquibase.update();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("DataBase error: " + e.getMessage());
        } catch (LiquibaseException e) {
            throw new LiquibaseProcessException("Liquibase error: " + e.getMessage());
        }
    }
}
