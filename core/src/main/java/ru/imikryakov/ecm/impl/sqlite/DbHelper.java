package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

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
            connection = DriverManager.getConnection("jdbc:sqlite:" + SqliteHierarchy.DB_NAME);
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

    void init(boolean dropExisting) {
        try {
            Statement stmt = connection.createStatement();
            String dropQuery = "DROP TABLE IF EXISTS CONTAINABLE";
            String createQuery = "CREATE TABLE IF NOT EXISTS Containable " +
                    "(ID VARCHAR(100) PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(100) NOT NULL, " +
                    "PARENT_ID VARCHAR(100), " +
                    "IS_CURRENT BOOLEAN, " +
                    "TYPE INT)";
            if (dropExisting)
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
        setValue(query, args);
    }
}
