package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;

import java.sql.*;

public class DbHelper {
    private static Logger logger = LogManager.getLogger();

    private static String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
    private static final String CONNECTION_URL = "jdbc:sqlite:" + Config.getProperty(Properties.SQLITE_DB_NAME);
    private static DbHelper instance = null;

    private Connection connection;

    public static synchronized DbHelper getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHelper();
        return instance;
    }

    private DbHelper() {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(CONNECTION_URL);
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

    private void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private void init() throws SQLException {
        Statement stmt = connection.createStatement();
        String queryFolder = "CREATE IF NOT EXISTS TABLE Folder " +
                "(ID INT PRIMARY KEY NOT NULL, " +
                "NAME VARCHAR(100) NOT NULL, " +
                "PARENT_ID INT)";
        String queryDocument = "CREATE IF NOT EXISTS TABLE Document " +
                "(ID INT PRIMARY KEY NOT NULL, " +
                "NAME VARCHAR(100) NOT NULL, " +
                "PARENT_ID INT)";
        stmt.executeUpdate(queryFolder);
        stmt.executeUpdate(queryDocument);
        stmt.close();
    }

    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }
}
