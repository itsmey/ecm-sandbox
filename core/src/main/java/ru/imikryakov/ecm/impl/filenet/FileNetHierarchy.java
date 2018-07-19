package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.security.auth.Subject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FileNetHierarchy implements FolderHierarchy {
    private static Logger logger = LogManager.getLogger();

    private Map<Id, com.filenet.api.core.Containable> cache = new HashMap<>();

    private UserContext userContext;
    private ObjectStore objectStore;

    private Id currentId;

    FileNetHierarchy(String objectStoreName) {
        String uri = Config.getProperty(Properties.FILENET_URI, true);
        String login = Config.getProperty(Properties.FILENET_LOGIN, true);
        String password = Config.getProperty(Properties.FILENET_PASSWORD, true);

        Connection conn = Factory.Connection.getConnection(uri);
        userContext = UserContext.get();
        Subject subject = UserContext.createSubject(conn, login, password, null);

        userContext.pushSubject(subject);

        Domain domain = Factory.Domain.getInstance(conn, null);
        objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);

        String nameProperty = null;
        ClassDescription cd = Factory.ClassDescription.fetchInstance(objectStore, "Document", null);
        if (cd.get_NamePropertyIndex() != null) {
            nameProperty =
                    ((PropertyDescription)cd.get_PropertyDescriptions().get(cd.get_NamePropertyIndex())).get_SymbolicName();
        }

        logger.trace("name property for Document is " + nameProperty);
        FileNetDocument.initPropertyFilter(nameProperty);

        setRootAsCurrent();
    }

    @Override
    public Folder getRootFolder() {
        return new FileNetFolder(objectStore.get_RootFolder().get_Id(), objectStore, cache);
    }

    @Override
    public void setRootAsCurrent() {
        setCurrentFolder(getRootFolder());
    }

    @Override
    public Folder getCurrentFolder() {
        return new FileNetFolder(currentId, objectStore, cache);
    }

    @Override
    public void setCurrentFolder(Folder f) {
        currentId = ((FileNetFolder)f).getId();
    }

    @Override
    public String getCurrentPath() {
//        Folder p = getCurrentFolder();
//        String path = p.getName();
//        while (p.getParent() != null) {
//            p = p.getParent();
//            path = p.getName() + "/" + path;
//        }
//        return path;
        return new FileNetFolder(currentId, objectStore, cache).ceFolder().get_PathName();
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
        Document d = new FileNetDocument(name, objectStore, cache);
        d.setParent(parent);
        return d;
    }

    @Override
    public Folder createFolder(String name) {
        return createFolder(name, getCurrentFolder());
    }

    @Override
    public Folder createFolder(String name, Folder parent) {
        return new FileNetFolder(name, parent, objectStore, cache);
    }

    @Override
    public void close() {
        if (userContext != null)
            userContext.popSubject();
    }
}
