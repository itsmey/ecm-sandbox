package ru.imikryakov.ecm.impl.simple;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.util.Comparator;
import java.util.List;

public class SimpleHierarchy implements FolderHierarchy {
    private final Folder rootFolder;
    private Folder currentFolder;

    public SimpleHierarchy() {
        rootFolder = SimpleFolder.createRoot();
        currentFolder = rootFolder;
    }

    @Override
    public Folder getRootFolder() {
        return rootFolder;
    }

    @Override
    public void setRootAsCurrent() {
        currentFolder = rootFolder;
    }

    @Override
    public Folder getCurrentFolder() {
        return currentFolder;
    }

    @Override
    public void setCurrentFolder(Folder f) {
        currentFolder = f;
    }

    @Override
    public String getCurrentPath() {
        Folder p = currentFolder;
        String path = currentFolder.getName();
        while (p.getParent() != null) {
            p = p.getParent();
            path = p.getName() + "/" + path;
        }
        return path;
    }

    @Override
    public List<Containable> list() {
        return currentFolder.getChildren();
    }

    @Override
    public void goToFolder(String name) {
        for (Containable child : currentFolder.getChildren()) {
            if (child instanceof Folder && child.getName().equals(name)) {
                currentFolder = (Folder)child;
            }
        }
    }

    @Override
    public void up() {
        if (currentFolder.getParent() != null) {
            currentFolder = currentFolder.getParent();
        }
    }

    @Override
    public void createDocument(String name) {
        currentFolder.addChild(new SimpleDocument(name));
    }

    @Override
    public void createFolder(String name) {
        currentFolder.addChild(new SimpleFolder(name));
    }

    @Override
    public Comparator<Containable> getComparator() {
        return (o1, o2) -> {
            if (o1 instanceof Folder && o2 instanceof Document)
                return -1;
            else if (o1 instanceof Document && o2 instanceof Folder)
                return 1;
            else
                return o1.getName().compareTo(o2.getName());
        };
    }
}
