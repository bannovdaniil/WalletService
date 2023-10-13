package ru.ylab.util;

/**
 * Загружаем свойства файла параметров в память.
 */
public interface PropertiesUtil {

    /**
     * Вернуть значение параметра по названию
     * @param key - название ключа
     * @return - значение параметра
     */
    String getProperties(String key);
}
