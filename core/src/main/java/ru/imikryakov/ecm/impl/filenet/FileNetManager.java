package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.*;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

class FileNetManager {
    private static Logger logger = LogManager.getLogger();

    private String objectStoreName;
    private ObjectStore objectStore;
    private UserContext userContext;

    private PropertyFilter documentPropertyFilter;
    private PropertyFilter folderPropertyFilter;
    private String documentNameProperty;

    private final int MAX_RECONNECTIONS = 5;
    private int reconnections;

    FileNetManager(String objectStoreName) {
        this.objectStoreName = objectStoreName;

        connect();

        initDocumentNameProperty();
        initDocumentPropertyFilter();
        initFolderPropertyFilter();
    }

    private void connect() {
        String uri = Config.getProperty(Properties.FILENET_URI, true);
        String login = Config.getProperty(Properties.FILENET_LOGIN, true);
        String password = Config.getProperty(Properties.FILENET_PASSWORD, true);

        Connection conn = Factory.Connection.getConnection(uri);
        userContext = UserContext.get();
        Subject subject = UserContext.createSubject(conn, login, password, "FileNetP8WSI");

        userContext.pushSubject(subject);

        Domain domain = Factory.Domain.getInstance(conn, null);
        objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);
    }

    void disconnect() {
        if (userContext != null)
            userContext.popSubject();
        userContext = null;
        objectStore = null;
    }

    private void reconnect() {
        logger.trace("reconnecting..");
        reconnections++;
        disconnect();
        connect();
    }

    String getDocumentNameProperty() {
        return documentNameProperty;
    }

    Folder getRootFolder() {
        return objectOperation(() -> Factory.Folder.fetchInstance(objectStore, "/Components", null));
    }

    Folder createFolder(Id id) {
        return objectOperation(() -> Factory.Folder.createInstance(objectStore, "Folder", id));
    }

    Folder fetchFolder(Id id) {
        return objectOperation(() -> Factory.Folder.fetchInstance(objectStore, id, folderPropertyFilter));
    }

    Document createDocument(Id id) {
        return objectOperation(() -> Factory.Document.createInstance(objectStore, "Document", id));
    }

    Document fetchDocument(Id id) {
        return objectOperation(() -> Factory.Document.fetchInstance(objectStore, id, documentPropertyFilter));
    }

    void refresh(IndependentlyPersistableObject obj) {
        PropertyFilter pf = getPropertyFilter(obj.get_ClassDescription().get_SymbolicName());
        objectOperation(() -> {
            obj.refresh(pf);
            return obj;
        });
    }

    void save(IndependentlyPersistableObject obj) {
        objectOperation(() -> {
            obj.save(RefreshMode.REFRESH);
            return obj;
        });
    }

    void file(Document document, Folder folder, String containmentName) {
        objectOperation(() -> {
            ReferentialContainmentRelationship rcr =
                folder.file(document, AutoUniqueName.NOT_AUTO_UNIQUE, containmentName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
            rcr.save(RefreshMode.REFRESH);
            return rcr;
        });
    }

    private PropertyFilter getPropertyFilter(String classId) {
        switch (classId) {
            case "Document": return documentPropertyFilter;
            case "Folder": return folderPropertyFilter;
            default: return null;
        }
    }

    private <T> T objectOperation(Supplier<T> function) {
        try {
            T result = function.get();
            reconnections = 0;
            return result;
        } catch (EngineRuntimeException e) {
            if (e.getExceptionCode() == ExceptionCode.SECURITY_INVALID_CREDENTIALS) {
                if (reconnections < MAX_RECONNECTIONS) {
                    reconnect();
                    return objectOperation(function);
                } else {
                    reconnections = 0;
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private void initDocumentNameProperty() {
        ClassDescription cd = Factory.ClassDescription.fetchInstance(objectStore, "Document", null);
        if (cd.get_NamePropertyIndex() != null) {
            documentNameProperty =
                    ((PropertyDescription)cd.get_PropertyDescriptions().get(cd.get_NamePropertyIndex())).get_SymbolicName();
        }

        logger.trace("name property for Document is " + documentNameProperty);
    }

    private void initDocumentPropertyFilter() {
        logger.trace("initPropertyFilter");
        documentPropertyFilter = new PropertyFilter();
        List<String> props = Arrays.asList("Id", "FoldersFiledIn", "ClassDescription");
        props = new ArrayList<>(props);
        if (documentNameProperty != null && !props.contains(documentNameProperty)) {
            logger.trace("added " + documentNameProperty);
            props.add(documentNameProperty);
        }
        for (String s : props) {
            documentPropertyFilter.addIncludeProperty(new FilterElement(null, null, null, s, null));
        }
    }

    private void initFolderPropertyFilter() {
        folderPropertyFilter = new PropertyFilter();
        List<String> props = Arrays.asList("SubFolders", "ContainedDocuments", "Id", "PathName", "Parent", "Name", "ClassDescription");
        for (String s : props) {
            folderPropertyFilter.addIncludeProperty(new FilterElement(null, null, null, s, null));
        }
    }
}
