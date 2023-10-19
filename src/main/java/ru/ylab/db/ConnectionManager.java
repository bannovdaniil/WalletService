package ru.ylab.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Для обеспечения первоначального обеспечения с базой данных, а также получение соединений для обращений к базе данных.
 */
public interface ConnectionManager {
    Connection getConnection() throws SQLException;
}
