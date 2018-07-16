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
    Document createDocument(String name);
    Document createDocument(String name, Folder parent);
    Folder createFolder(String name);
    Folder createFolder(String name, Folder parent);
    Comparator<Containable> getComparator();
    void export(String filename);
    void close();
}
