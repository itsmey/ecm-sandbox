package ru.imikryakov.ecm;

import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;
import ru.imikryakov.ecm.impl.simple.SimpleHierarchyFactory;
import ru.imikryakov.ecm.types.FolderHierarchyFactory;

public class FolderHierarchyManager {
    public static FolderHierarchyFactory get() {
        String type = Config.getProperty(Properties.HierarchyType.TITLE);
        assert type != null;
        switch (type) {
            case Properties.HierarchyType.SIMPLE: return new SimpleHierarchyFactory();
            default: throw new RuntimeException("invalid hierarchy type: " + type);
        }
    }
}
