package ru.ylab.util.impl;

import ru.ylab.util.PropertiesUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * {@inheritDoc}
 */
public final class ApplicationPropertiesUtilImpl implements PropertiesUtil {
    private static PropertiesUtil instance;
    private static final Properties PROPERTIES = new Properties();
    public static final String DRIVER_CLASS_KEY = "db.driver-class-name";
    public static final String URL_KEY = "db.url";
    public static final String USERNAME_KEY = "db.username";
    public static final String PASSWORD_KEY = "db.password";
    public static final String DEFAULT_SCHEMA_NAME = "liquibase.defaultSchemaName";
    public static final String LIQUIBASE_SCHEMA_NAME = "liquibase.liquibaseSchemaName";

    /**
     * Имя файла для загрузки свойств.
     */
    private static final String PROPERTIES_FILE = "application.properties";

    private ApplicationPropertiesUtilImpl() {
        loadProperties();
    }

    public static synchronized PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new ApplicationPropertiesUtilImpl();
        }
        return instance;
    }

    @Override
    public String getProperties(String key) {
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
