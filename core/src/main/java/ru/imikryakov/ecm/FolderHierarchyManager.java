package ru.imikryakov.ecm;

import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;
import ru.imikryakov.ecm.impl.simple.SimpleHierarchy;
import ru.imikryakov.ecm.types.FolderHierarchy;

public class FolderHierarchyManager {
    public static FolderHierarchyFactory get() {
        String type = Config.getProperty(Properties.HierarchyType.TITLE);
        assert type != null;
        switch (type) {
            case Properties.HierarchyType.SIMPLE: return SIMPLE;
            default: throw new RuntimeException("invalid hierarchy type: " + type);
        }
    }

    final private static FolderHierarchyFactory SIMPLE = new FolderHierarchyFactory() {
        @Override
        public FolderHierarchy createEmpty() {
            return SimpleHierarchy.empty();
        }

        @Override
        public FolderHierarchy createRandomized() {
            FolderHierarchy hierarchy = SimpleHierarchy.empty();
            HierarchyRandomizer.populate(hierarchy);
            return hierarchy;
        }

        @Override
        public FolderHierarchy createFromXML(String filename) {
            return SimpleHierarchy.fromXml(filename);
        }
    };
}
