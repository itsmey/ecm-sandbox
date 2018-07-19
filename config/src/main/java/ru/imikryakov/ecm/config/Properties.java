package ru.imikryakov.ecm.config;

public interface Properties {
    interface HierarchyType {
        String TITLE = "hierarchy.type";

        String SIMPLE = "SIMPLE";
        String SQLITE = "SQLITE";
        String FILENET = "FILENET";
    }

    String SQLITE_DB_NAME = "hierarchy.sqlite.db_name";

    String FILENET_URI = "hierarchy.filenet.uri";
    String FILENET_LOGIN = "hierarchy.filenet.login";
    String FILENET_PASSWORD = "hierarchy.filenet.password";
    String FILENET_OBJECT_STORE_NAME = "hierarchy.filenet.objectStoreName";
}
