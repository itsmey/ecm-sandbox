package ru.imikryakov.ecm;

import ru.imikryakov.ecm.impl.simple.SimpleHierarchy;
import ru.imikryakov.ecm.types.FolderHierarchy;

public class FolderHierarchyFactory {
    public static FolderHierarchy createSimpleHierarchy() {
        return new SimpleHierarchy();
    }
}
