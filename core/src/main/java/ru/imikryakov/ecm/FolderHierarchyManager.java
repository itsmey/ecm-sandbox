package ru.imikryakov.ecm;

import ru.imikryakov.ecm.impl.simple.SimpleHierarchy;
import ru.imikryakov.ecm.types.FolderHierarchy;

public class FolderHierarchyManager {
    final public static FolderHierarchyFactory SIMPLE = new FolderHierarchyFactory() {
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
