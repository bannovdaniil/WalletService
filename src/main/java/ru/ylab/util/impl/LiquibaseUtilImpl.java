package ru.ylab.util.impl;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ylab.db.ConnectionManager;
import ru.ylab.exception.DatabaseConnectionException;
import ru.ylab.exception.LiquibaseProcessException;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * {@inheritDoc}
 */
@Component
@RequiredArgsConstructor
public class LiquibaseUtilImpl implements LiquibaseUtil {
    private final PropertiesUtil propertiesUtil;
    private final ConnectionManager connectionManager;
    private static final String CHANGE_LOGFILE_NAME = "db/changelog/changelog.xml";

    @Override
    @SuppressWarnings("squid:S1874")
    public synchronized void init() {

        try {
            Connection connection = connectionManager.getConnection();

            initLiquibaseSchema(connection);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.DEFAULT_SCHEMA_NAME));
            database.setLiquibaseSchemaName(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.LIQUIBASE_SCHEMA_NAME));
            Liquibase liquibase = new Liquibase(CHANGE_LOGFILE_NAME, new ClassLoaderResourceAccessor(), database);

            liquibase.update();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("DataBase error: " + e.getMessage());
        } catch (LiquibaseException e) {
            throw new LiquibaseProcessException("Liquibase error: " + e.getMessage());
        }
    }

    /**
     * Создает необходимые для работы схемы базы данных.
     */
    private void initLiquibaseSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlSchema = String.format("CREATE SCHEMA IF NOT EXISTS %s; " +
                                             "CREATE SCHEMA IF NOT EXISTS %s;",
                    propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.DEFAULT_SCHEMA_NAME),
                    propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.LIQUIBASE_SCHEMA_NAME));

            statement.execute(sqlSchema);
        }
    }
}
