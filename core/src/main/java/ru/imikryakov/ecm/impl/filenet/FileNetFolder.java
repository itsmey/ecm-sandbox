package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;

import java.util.*;

class FileNetFolder extends FileNetContainable implements Folder {
    private static Logger logger = LogManager.getLogger();

    FileNetFolder(String name, Folder parent, ObjectStore objectStore, Map<Id, com.filenet.api.core.Containable> cache) {
        super(objectStore, cache);
        ceFolder().set_FolderName(name);
        ceFolder().set_Parent(((FileNetFolder)parent).ceFolder());
        ceInstance().save(RefreshMode.REFRESH, getPropertyFilter());
        cache.put(instance.get_Id(), instance);
    }

    FileNetFolder(Id id, ObjectStore objectStore, Map<Id, com.filenet.api.core.Containable> cache) {
        super(id, objectStore, cache);
    }

    private static PropertyFilter propertyFilter;

    static {
        initPropertyFilter();
    }

    @Override
    public PropertyFilter getPropertyFilter() {
        return propertyFilter;
    }

    private static void initPropertyFilter() {
        propertyFilter = new PropertyFilter();
        List<String> props = Arrays.asList("SubFolders", "ContainedDocuments", "Id", "PathName", "Parent", "Name");
        for (String s : props) {
            propertyFilter.addIncludeProperty(new FilterElement(null, null, null, s, null));
        }
    }

    @Override
    public com.filenet.api.core.Containable fetchInstance() {
        return Factory.Folder.fetchInstance(objectStore, id, propertyFilter);
    }

    @Override
    public com.filenet.api.core.Containable createInstance() {
        return Factory.Folder.createInstance(objectStore, "Folder", id);
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
            return new FileNetFolder(ceFolder().get_Parent().get_Id(), objectStore, cache);
        }
    }

    @Override
    public void setParent(Folder parent) {
        ceFolder().set_Parent(new FileNetFolder(((FileNetFolder)parent).getId(), objectStore, cache).ceFolder());
        ceInstance().save(RefreshMode.REFRESH, propertyFilter);
    }

    @Override
    public List<Containable> getChildren() {
        List<Containable> result = new ArrayList<>();
        Iterator i = ceFolder().get_SubFolders().iterator();
        while (i.hasNext()) {
            com.filenet.api.core.Folder f = (com.filenet.api.core.Folder)i.next();
//            logger.trace(f.get_PathName());
            result.add(new FileNetFolder(f.get_Id(), objectStore, cache));
        }
        i = ceFolder().get_ContainedDocuments().iterator();
        while (i.hasNext()) {
            com.filenet.api.core.Document f = (com.filenet.api.core.Document)i.next();
            result.add(new FileNetDocument(f.get_Id(), objectStore, cache));
        }
        return result;
    }

    @Override
    public Containable addChild(Containable child) {
        child.setParent(this);
        return child;
    }
}
