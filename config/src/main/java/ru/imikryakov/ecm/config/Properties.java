package ru.imikryakov.ecm.config;

public interface Properties {
    interface HierarchyType {
        String TITLE = "hierarchy.type";
        String SIMPLE = "SIMPLE";
    }

    String SQLITE_DB_NAME = "hierarchy.sqlite.dbname";
}
