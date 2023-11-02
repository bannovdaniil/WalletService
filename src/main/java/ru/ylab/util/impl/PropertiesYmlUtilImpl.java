package ru.ylab.util.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.ylab.util.PropertiesUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Component
@Slf4j
public final class PropertiesYmlUtilImpl implements PropertiesUtil {
    public static final String DEFAULT_SCHEMA = "spring.";
    private static final Yaml yaml = new Yaml();
    /**
     * Имя файла для загрузки свойств.
     */
    private static final String PROPERTIES_FILE = "application.yml";
    private static final Map<String, String> properties = new HashMap<>();

    public PropertiesYmlUtilImpl() {
        loadProperties();
    }

    /**
     * Собираем все свойства в одну мапу
     */
    @SuppressWarnings("squid:S3740")
    private static void extractedYamlProperty(Map<Object, Object> yamlMap, String keyPath) {
        for (Map.Entry entry : yamlMap.entrySet()) {
            String newKeyPath = keyPath.concat(keyPath.isBlank() ? "" : ".").concat((String) entry.getKey());
            if (entry.getValue() instanceof String || entry.getValue() instanceof Integer) {
                properties.put(newKeyPath, entry.getValue().toString());
            } else if (entry.getValue() instanceof LinkedHashMap<?, ?>) {
                extractedYamlProperty((Map) entry.getValue(), newKeyPath);
            } else {
                log.error("Unknown type yamlMap = " + yamlMap);
            }
        }
    }

    @Override
    public String getProperties(String... key) {
        if (key.length == 0) {
            return "";
        }
        if (System.getenv(key[0]) != null) {
            return System.getenv(key[0]);
        }
        if (System.getProperty(key[0]) != null) {
            return System.getProperty(key[0]);
        }
        if (key.length == 2) {
            return properties.get(DEFAULT_SCHEMA.concat(key[1]).concat(".").concat(key[0]));
        }
        return properties.get(DEFAULT_SCHEMA.concat(key[0]));
    }

    /**
     *
     */
    private void loadProperties() {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE);
        Map<Object, Object> yamlProperties = yaml.load(inputStream);
        log.info("Property: {}", yamlProperties);
        extractedYamlProperty(yamlProperties, "");
    }

}
