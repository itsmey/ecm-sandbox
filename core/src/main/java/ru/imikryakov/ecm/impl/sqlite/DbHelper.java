package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.JDBC;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DbHelper {
    private static Logger logger = LogManager.getLogger();
    private static DbHelper instance = null;
    private Connection connection;

    public static synchronized DbHelper getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHelper();
        return instance;
    }

    private DbHelper() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection("jdbc:sqlite:" + Config.getProperty(Properties.SQLITE_DB_NAME));
        init();
    }

    private void init() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS document (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name VARCHAR(100) NOT NULL," +
                                "parent INTEGER PRIMARY KEY NOT NULL);");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS folder (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name VARCHAR(100) NOT NULL);");
        stmt.close();
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
