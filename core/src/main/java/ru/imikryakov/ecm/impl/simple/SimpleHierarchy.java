package ru.imikryakov.ecm.impl.simple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.HierarchyXmlDescription;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.Comparator;
import java.util.List;

public class SimpleHierarchy implements FolderHierarchy {
    private static Logger logger = LogManager.getLogger();
    private Folder rootFolder;
    private Folder currentFolder;

    protected SimpleHierarchy() {
        this.rootFolder = createFolder("Root", null);
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
                return;
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
    public Document createDocument(String name) {
        return createDocument(name, currentFolder);
    }

    @Override
    public Document createDocument(String name, Folder parent) {
        Document d = new SimpleDocument(name);
        if (parent != null)
            parent.addChild(d);
        return d;
    }

    @Override
    public Folder createFolder(String name) {
        return createFolder(name, currentFolder);
    }

    @Override
    public Folder createFolder(String name, Folder parent) {
        Folder f = new SimpleFolder(name);
        if (parent != null)
            parent.addChild(f);
        return f;
    }

    @Override
    public void close() {
    }
}
