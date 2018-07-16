package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;
import ru.imikryakov.ecm.impl.simple.SimpleHierarchyFactory;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.io.File;
import java.util.Objects;

public class SqliteHierarchyFactory extends SimpleHierarchyFactory {
    private static Logger logger = LogManager.getLogger();

    @Override
    public FolderHierarchy createEmpty() {
        File dbFile = new File(Objects.requireNonNull(Config.getProperty(Properties.SQLITE_DB_NAME)));
        if (dbFile.exists()) {
            logger.warn("DB file exists!");
        }
        return new SqliteHierarchy();
    }
}
