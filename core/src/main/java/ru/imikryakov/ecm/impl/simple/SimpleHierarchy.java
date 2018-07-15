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
        Document d = new SimpleDocument(name);
        currentFolder.addChild(new SimpleDocument(name));
        return d;
    }

    @Override
    public Document createDocument(String name, Folder parent) {
        Document d = new SimpleDocument(name);
        if (parent != null)
            parent.addChild(new SimpleDocument(name));
        return d;
    }

    @Override
    public Folder createFolder(String name) {
        Folder f = new SimpleFolder(name);
        currentFolder.addChild(new SimpleFolder(name));
        return f;
    }

    @Override
    public Folder createFolder(String name, Folder parent) {
        Folder f = new SimpleFolder(name);
        if (parent != null)
            parent.addChild(new SimpleFolder(name));
        return f;
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

    @Override
    public void export(String filename) {
        try {
            HierarchyXmlDescription description = new HierarchyXmlDescription(this);
            JAXBContext jc = JAXBContext.newInstance(HierarchyXmlDescription.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(description, new File(filename));
        } catch (JAXBException e) {
            logger.error(e);

        }
    }
}
