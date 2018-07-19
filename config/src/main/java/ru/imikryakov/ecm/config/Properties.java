package ru.imikryakov.ecm.config;

public interface Properties {
    interface HierarchyType {
        String TITLE = "hierarchy.type";

        String SIMPLE = "SIMPLE";
        String SQLITE = "SQLITE";
        String FILENET = "FILENET";
    }

    interface HierarchyFactory {
        String TITLE = "hierarchy.factory";

        String EMPTY = "EMPTY";
        String RANDOMIZED = "RANDOMIZED";
        String FROM_XML = "FROM_XML";
        String EXISTING = "EXISTING";
    }

    String SQLITE_DB_NAME = "hierarchy.sqlite.db_name";

    String FILENET_URI = "hierarchy.filenet.uri";
    String FILENET_LOGIN = "hierarchy.filenet.login";
    String FILENET_PASSWORD = "hierarchy.filenet.password";
    String FILENET_OBJECT_STORE_NAME = "hierarchy.filenet.objectStoreName";
}
