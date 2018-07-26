package ru.imikryakov.ecm.impl.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.impl.filenet.FileNetContainable;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;

import java.util.Map;
import java.util.UUID;

abstract class SqliteContainable implements Containable {
    static Logger logger = LogManager.getLogger();
    String id;
    DbHelper dbHelper;

    SqliteContainable(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        id = UUID.randomUUID().toString();
    }

    SqliteContainable(String id, DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.id = id;
    }

    @Override
    public Folder getParent() {
        String parentId = (String)dbHelper.getValue("SELECT PARENT_ID FROM CONTAINABLE WHERE ID = ?", "PARENT_ID", id);
        if (parentId == null) {
            return null;
        }
        return new SqliteFolder((String)dbHelper.getValue("SELECT PARENT_ID FROM CONTAINABLE WHERE ID = ?", "PARENT_ID", id), dbHelper);
    }

    @Override
    public void setParent(Folder parent) {
        dbHelper.setValue("UPDATE CONTAINABLE SET PARENT_ID = ? WHERE ID = ?", ((SqliteFolder)parent).getId(), id);
    }

    @Override
    public String getName() {
        return (String)dbHelper.getValue("SELECT NAME FROM CONTAINABLE WHERE ID = ?", "NAME", id);
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SqliteContainable)
            return hashCode() == obj.hashCode();
        else
            return false;
    }
}
