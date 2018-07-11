package ru.imikryakov.ecm.types;

import java.util.Comparator;
import java.util.List;

public interface FolderHierarchy {
    Folder getRootFolder();
    void setRootAsCurrent();
    Folder getCurrentFolder();
    void setCurrentFolder(Folder f);
    String getCurrentPath();
    List<Containable> list();
    void goToFolder(String name);
    void up();
    void createDocument(String name);
    void createFolder(String name);
    Comparator<Containable> getComparator();
}
