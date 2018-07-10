package ru.imikryakov.ecm.impl.simple;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.util.List;

public class SimpleHierarchy implements FolderHierarchy {
    private Folder rootFolder;
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
    public Folder getCurrentFolder() {
        return currentFolder;
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
}
