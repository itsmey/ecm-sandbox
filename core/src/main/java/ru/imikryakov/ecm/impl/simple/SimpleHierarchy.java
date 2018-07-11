package ru.imikryakov.ecm.impl.simple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

public class SimpleHierarchy implements FolderHierarchy {
    private static Logger logger = LogManager.getLogger();
    private final Folder rootFolder;
    private Folder currentFolder;

    public SimpleHierarchy() {
        rootFolder = SimpleFolder.createRoot();
        currentFolder = rootFolder;
    }

    public static FolderHierarchy fromXml(String fileName) {
        logger.trace("fromXml");
        try {
            JAXBContext jc = JAXBContext.newInstance();
            Unmarshaller u = jc.createUnmarshaller();
            SimpleHierarchyXmlDescription description = (SimpleHierarchyXmlDescription )u.unmarshal(new FileInputStream(fileName));
            return fromDescription(description);
        } catch (JAXBException | FileNotFoundException e) {
            logger.error(e);
            return null;
        }
    }

    private static FolderHierarchy fromDescription(SimpleHierarchyXmlDescription description) {
        return new SimpleHierarchy();
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
