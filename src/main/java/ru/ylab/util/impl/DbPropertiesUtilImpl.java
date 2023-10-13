package ru.ylab.util.impl;

import ru.ylab.util.PropertiesUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * {@inheritDoc}
 */
public final class DbPropertiesUtilImpl implements PropertiesUtil {
    private static PropertiesUtil instance;
    private static final Properties PROPERTIES = new Properties();
    /**
     * Имя файла для загрузки свойств.
     */
    private static final String PROPERTIES_FILE = "db.properties";

    private DbPropertiesUtilImpl() {
        loadProperties();
    }

    public static synchronized PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new DbPropertiesUtilImpl();
        }
        return instance;
    }

    @Override
    public String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties() {
        try (InputStream inFile = DbPropertiesUtilImpl.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inFile);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
