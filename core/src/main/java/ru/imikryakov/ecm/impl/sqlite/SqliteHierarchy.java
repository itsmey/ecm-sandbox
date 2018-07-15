package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.impl.simple.SimpleHierarchy;
import ru.imikryakov.ecm.types.Folder;

import java.sql.SQLException;

public class SqliteHierarchy extends SimpleHierarchy {
    private static Logger logger = LogManager.getLogger();
    private DbHelper dbHelper = null;

    SqliteHierarchy(Folder rootFolder) {
        super(rootFolder);
        try {
            dbHelper = DbHelper.getInstance();
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

}
