package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;

import java.sql.*;
import java.util.Arrays;

class DbHelper {
    private static Logger logger = LogManager.getLogger();

    static int TYPE_FOLDER = 0;
    static int TYPE_DOCUMENT = 1;

    private static DbHelper instance = null;

    private Connection connection;

    static synchronized DbHelper getInstance() {
        if (instance == null)
            instance = new DbHelper();
        return instance;
    }

    private DbHelper() {
        try {
            String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection("jdbc:sqlite:" + Config.getProperty(Properties.SQLITE_DB_NAME));
            init();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e);
            try {
                close();
            } catch (SQLException e2) {
                logger.error(e);
                throw new RuntimeException(e);
            }
            throw new RuntimeException(e);
        }
    }

    void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    void init() {
        try {
            Statement stmt = connection.createStatement();
            String dropQuery = "DROP TABLE IF EXISTS CONTAINABLE";
            String createQuery = "CREATE TABLE IF NOT EXISTS Containable " +
                    "(ID VARCHAR(100) PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(100) NOT NULL, " +
                    "PARENT_ID VARCHAR(100), " +
                    "IS_CURRENT BOOLEAN, " +
                    "TYPE INT)";
            stmt.executeUpdate(dropQuery);
            stmt.executeUpdate(createQuery);
            stmt.close();
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    PreparedStatement getPreparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    Object getValue(String query, String fieldName, Object... args) {
        logger.trace("getValue: " + query + " | " + fieldName + " | " + Arrays.toString(args));
        try {
            PreparedStatement stmt = getPreparedStatement(query);
            int i = 1;
            for (Object arg : args) {
                stmt.setObject(i++, arg);
            }
            ResultSet rs = stmt.executeQuery();
            rs.next();
            Object value = rs.getObject(fieldName);
            stmt.close();
            return value;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    void setValue(String query, Object... args) {
        logger.trace("setValue: " + query + " | " + Arrays.toString(args));
        try {
            PreparedStatement stmt = getPreparedStatement(query);
            int i = 1;
            for (Object arg : args) {
                stmt.setObject(i++, arg);
            }
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    void insert(String query, Object... args) {
        logger.trace("insert: " + query + " | " + Arrays.toString(args));
        setValue(query, args);
    }
}
