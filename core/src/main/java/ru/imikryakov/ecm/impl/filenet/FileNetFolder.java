package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;

import java.util.*;

class FileNetFolder extends FileNetContainable implements Folder {
    private static Logger logger = LogManager.getLogger();

    FileNetFolder(String name, Folder parent, FileNetManager filenet, Map<Id, com.filenet.api.core.Containable> cache) {
        super(filenet, cache);
        ceFolder().set_FolderName(name);
        ceFolder().set_Parent(((FileNetFolder)parent).ceFolder());
        filenet.save(ceInstance());
        cache.put(instance.get_Id(), instance);
    }

    FileNetFolder(Id id, FileNetManager filenet, Map<Id, com.filenet.api.core.Containable> cache) {
        super(id, filenet, cache);
    }

    @Override
    public com.filenet.api.core.Containable fetchInstance() {
        return filenet.fetchFolder(id);
    }

    @Override
    public com.filenet.api.core.Containable createInstance() {
        return filenet.createFolder(id);
    }

    @Override
    public IndependentlyPersistableObject ceInstance() {
        return (com.filenet.api.core.Folder)instance;
    }

    com.filenet.api.core.Folder ceFolder() {
        return (com.filenet.api.core.Folder)instance;
    }

    @Override
    public Folder getParent() {
        if (ceFolder().get_Parent() == null) {
            return null;
        } else {
            return new FileNetFolder(ceFolder().get_Parent().get_Id(), filenet, cache);
        }
    }

    @Override
    public void setParent(Folder parent) {
        ceFolder().set_Parent(new FileNetFolder(((FileNetFolder)parent).getId(), filenet, cache).ceFolder());
        filenet.save(ceInstance());
    }

    @Override
    public List<Containable> getChildren() {
        List<Containable> result = new ArrayList<>();
        Iterator i = ceFolder().get_SubFolders().iterator();
        while (i.hasNext()) {
            com.filenet.api.core.Folder f = (com.filenet.api.core.Folder)i.next();
            result.add(new FileNetFolder(f.get_Id(), filenet, cache));
        }
        i = ceFolder().get_ContainedDocuments().iterator();
        while (i.hasNext()) {
            com.filenet.api.core.Document f = (com.filenet.api.core.Document)i.next();
            result.add(new FileNetDocument(f.get_Id(), filenet, cache));
        }
        return result;
    }

    @Override
    public Containable addChild(Containable child) {
        child.setParent(this);
        return child;
    }
}
