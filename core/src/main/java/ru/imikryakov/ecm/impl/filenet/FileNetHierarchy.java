package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FileNetHierarchy implements FolderHierarchy {
    private static Logger logger = LogManager.getLogger();

    private Map<Id, com.filenet.api.core.Containable> cache = new HashMap<>();
    private FileNetManager filenet;
    private Id currentId;

    FileNetHierarchy(String objectStoreName) {
        filenet = new FileNetManager(objectStoreName);
        setRootAsCurrent();
    }

    @Override
    public Folder getRootFolder() {
        return new FileNetFolder(filenet.getRootFolder().get_Id(), filenet, cache);
    }

    @Override
    public void setRootAsCurrent() {
        setCurrentFolder(getRootFolder());
    }

    @Override
    public Folder getCurrentFolder() {
        return new FileNetFolder(currentId, filenet, cache);
    }

    @Override
    public void setCurrentFolder(Folder f) {
        currentId = ((FileNetFolder)f).getId();
    }

    @Override
    public String getCurrentPath() {
        return new FileNetFolder(currentId, filenet, cache).ceFolder().get_PathName();
    }

    @Override
    public List<Containable> list() {
        return getCurrentFolder().getChildren();
    }

    @Override
    public void goToFolder(String name) {
        for (Containable child : getCurrentFolder().getChildren()) {
            if (child instanceof Folder && child.getName().equals(name)) {
                setCurrentFolder((Folder)child);
                return;
            }
        }
    }

    @Override
    public void up() {
        if (getCurrentFolder().getParent() != null) {
            setCurrentFolder(getCurrentFolder().getParent());
        }
    }

    @Override
    public Document createDocument(String name) {
        return createDocument(name, getCurrentFolder());
    }

    @Override
    public Document createDocument(String name, Folder parent) {
        Document d = new FileNetDocument(name, filenet, cache);
        d.setParent(parent);
        return d;
    }

    @Override
    public Folder createFolder(String name) {
        return createFolder(name, getCurrentFolder());
    }

    @Override
    public Folder createFolder(String name, Folder parent) {
        return new FileNetFolder(name, parent, filenet, cache);
    }

    @Override
    public void close() {
        if (filenet != null) {
            filenet.disconnect();
        }
    }
}
