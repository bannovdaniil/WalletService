package ru.ylab.util.impl;

import org.springframework.stereotype.Component;
import ru.ylab.util.PropertiesUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * {@inheritDoc}
 */
@Component
public final class ApplicationPropertiesUtilImpl implements PropertiesUtil {
    public static final String DRIVER_CLASS_KEY = "db.driver-class-name";
    public static final String URL_KEY = "DATASOURCE_URL";
    public static final String USERNAME_KEY = "POSTGRES_USER";
    public static final String PASSWORD_KEY = "POSTGRES_PASSWORD";
    public static final String DEFAULT_SCHEMA_NAME = "liquibase.defaultSchemaName";
    public static final String LIQUIBASE_SCHEMA_NAME = "liquibase.liquibaseSchemaName";
    private static final Properties PROPERTIES = new Properties();
    /**
     * Имя файла для загрузки свойств.
     */
    private static final String PROPERTIES_FILE = "application.properties";

    @Override
    public String getProperties(String key) {
        if (System.getenv(key) != null) {
            return System.getenv(key);
        }
        if (System.getProperty(key) != null) {
            return System.getProperty(key);
        }
        loadProperties();
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties() {
        try (InputStream inFile = ApplicationPropertiesUtilImpl.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inFile);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
