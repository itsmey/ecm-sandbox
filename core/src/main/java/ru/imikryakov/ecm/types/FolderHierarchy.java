package ru.imikryakov.ecm.types;

import java.util.List;

public interface FolderHierarchy {
    Folder getRootFolder();
    Folder getCurrentFolder();
    String getCurrentPath();
    List<Containable> list();
    void goToFolder(String name);
    void up();
    void createDocument(String name);
    void createFolder(String name);
}
