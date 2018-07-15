package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.impl.simple.SimpleHierarchy;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;

import java.sql.SQLException;

public class SqliteHierarchy extends SimpleHierarchy {
    private static Logger logger = LogManager.getLogger();
    private DbHelper dbHelper = null;

    SqliteHierarchy() {
        super();
        try {
            dbHelper = DbHelper.getInstance();
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Document createDocument(String name, Folder parent) {
        return super.createDocument(name, parent);
    }

    @Override
    public Folder createFolder(String name, Folder parent) {
        return super.createFolder(name, parent);
    }

    @Override
    public void close() {
        super.close();
        if (dbHelper != null) {
            try {
                dbHelper.close();
            } catch (SQLException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        }
    }
}
