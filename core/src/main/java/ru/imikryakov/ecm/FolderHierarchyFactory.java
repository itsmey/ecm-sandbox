package ru.imikryakov.ecm;

import ru.imikryakov.ecm.impl.simple.SimpleHierarchy;
import ru.imikryakov.ecm.types.FolderHierarchy;

public interface FolderHierarchyFactory {
    FolderHierarchy createEmpty();
    FolderHierarchy createRandomized();
    FolderHierarchy createFromXML(String filename);
}
