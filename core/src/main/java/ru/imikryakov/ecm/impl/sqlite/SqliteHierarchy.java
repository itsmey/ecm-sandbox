package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.sql.SQLException;
import java.util.List;

public class SqliteHierarchy implements FolderHierarchy {
    private static Logger logger = LogManager.getLogger();
    static String DB_NAME;
    private DbHelper dbHelper;

    SqliteHierarchy(String dbName) {
        DB_NAME = dbName;
        dbHelper = DbHelper.getInstance();
        dbHelper.init(false);
    }

    SqliteHierarchy() {
        DB_NAME = Config.getProperty(Properties.SQLITE_DB_NAME);
        dbHelper = DbHelper.getInstance();
        dbHelper.init(true);
        createFolder("Root", null);
        setRootAsCurrent();
    }

    @Override
    public Folder getRootFolder() {
        return new SqliteFolder((String)dbHelper.getValue("SELECT ID FROM CONTAINABLE WHERE PARENT_ID IS NULL", "ID"), dbHelper);
    }

    @Override
    public void setRootAsCurrent() {
        setCurrentFolder(getRootFolder());
    }

    @Override
    public Folder getCurrentFolder() {
        return new SqliteFolder((String)dbHelper.getValue("SELECT ID FROM CONTAINABLE WHERE IS_CURRENT IS true", "ID"), dbHelper);
    }

    @Override
    public void setCurrentFolder(Folder f) {
        dbHelper.setValue("UPDATE CONTAINABLE SET IS_CURRENT = ? WHERE IS_CURRENT IS true", false);
        dbHelper.setValue("UPDATE CONTAINABLE SET IS_CURRENT = ? WHERE ID = ?", true, ((SqliteContainable)f).getId());
    }

    @Override
    public String getCurrentPath() {
        Folder p = getCurrentFolder();
        String path = p.getName();
        while (p.getParent() != null) {
            p = p.getParent();
            path = p.getName() + "/" + path;
        }
        return path;
    }

    @Override
    public List<Containable> list() {
        return getCurrentFolder().getChildren();
    }

    @Override
    public void goToFolder(String name) {
        for (Containable child : getCurrentFolder().getChildren()) {
            if (child instanceof Folder && child.getName().equals(name)) {
                setCurrentFolder((Folder)child);
                return;
            }
        }
    }

    @Override
    public void up() {
        if (getCurrentFolder().getParent() != null) {
            setCurrentFolder(getCurrentFolder().getParent());
        }
    }

    @Override
    public Document createDocument(String name) {
        return createDocument(name, getCurrentFolder());
    }

    @Override
    public Document createDocument(String name, Folder parent) {
        Document d = new SqliteDocument(dbHelper);
        addContainable(((SqliteContainable)d).getId(), name, parent, DbHelper.TYPE_DOCUMENT);
        return d;
    }

    @Override
    public Folder createFolder(String name) {
        return createFolder(name, getCurrentFolder());
    }

    @Override
    public Folder createFolder(String name, Folder parent) {
        Folder f = new SqliteFolder(dbHelper);
        addContainable(((SqliteContainable)f).getId(), name, parent, DbHelper.TYPE_FOLDER);
        return f;
    }

    @Override
    public void close() {
        if (dbHelper != null) {
            try {
                dbHelper.close();
            } catch (SQLException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        }
    }

    private void addContainable(String id, String name, Folder parent, int type) {
        dbHelper.insert("INSERT INTO CONTAINABLE VALUES (?, ?, ?, ?, ?)", id, name, parent == null ? null :((SqliteFolder)parent).getId(), false, type);
    }
}
