package ru.imikryakov.ecm.types;

public interface FolderHierarchyFactory {
    FolderHierarchy createEmpty();
    FolderHierarchy createRandomized();
    FolderHierarchy createFromXML(String filename);
}
