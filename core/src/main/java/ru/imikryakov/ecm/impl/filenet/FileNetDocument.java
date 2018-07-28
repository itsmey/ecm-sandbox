package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.core.*;
import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;

import java.util.*;

class FileNetDocument extends FileNetContainable implements Document {
    private static Logger logger = LogManager.getLogger();

    FileNetDocument(String name, FileNetManager filenet, Map<Id, Containable> cache) {
        super(filenet, cache);
        String nameProperty = filenet.getDocumentNameProperty();
        if (nameProperty != null) {
            ceDocument().getProperties().putValue(nameProperty, name);
            logger.trace("put " + nameProperty + " = " + name);
        }
        filenet.save(ceDocument());
        cache.put(instance.get_Id(), instance);
    }

    FileNetDocument(Id id, FileNetManager filenet, Map<Id, Containable> cache) {
        super(id, filenet, cache);
    }

    @Override
    public com.filenet.api.core.Containable fetchInstance() {
        return filenet.fetchDocument(id);
    }

    @Override
    public com.filenet.api.core.Containable createInstance() {
        return filenet.createDocument(id);
    }

    @Override
    public IndependentlyPersistableObject ceInstance() {
        return (com.filenet.api.core.Document)instance;
    }

    private com.filenet.api.core.Document ceDocument() {
        return (com.filenet.api.core.Document)instance;
    }

    @Override
    public Folder getParent() {
        Iterator i = ceDocument().get_FoldersFiledIn().iterator();
        if (i.hasNext()) {
            com.filenet.api.core.Folder ceParent = (com.filenet.api.core.Folder)i.next();
            return new FileNetFolder(ceParent.get_Id(), filenet, cache);
        } else {
            return null;
        }

    }

    @Override
    public void setParent(Folder parent) {
        filenet.file(ceDocument(), ((FileNetFolder)parent).ceFolder(), getName());
    }

    @Override
    public String getName() {
        String nameProperty = filenet.getDocumentNameProperty();
        if (nameProperty != null) {
            return ceDocument().getProperties().getStringValue(nameProperty);
        } else {
            return getId().toString();
        }
    }
}
