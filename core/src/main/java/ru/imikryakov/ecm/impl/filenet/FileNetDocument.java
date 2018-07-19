package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;

import java.util.*;

class FileNetDocument extends FileNetContainable implements Document {
    private static Logger logger = LogManager.getLogger();

    FileNetDocument(String name, ObjectStore objectStore, Map<Id, Containable> cache) {
        super(objectStore, cache);
        if (nameProperty != null) {
            ceDocument().getProperties().putValue(nameProperty, name);
            logger.trace("put " + nameProperty + " = " + name);
        }
        ceDocument().save(RefreshMode.REFRESH, getPropertyFilter());
        cache.put(instance.get_Id(), instance);
    }

    FileNetDocument(Id id, ObjectStore objectStore, Map<Id, Containable> cache) {
        super(id, objectStore, cache);
    }

    private static PropertyFilter propertyFilter;
    private static String nameProperty = null;

    static void initPropertyFilter(String nameProperty) {
        logger.trace("initPropertyFilter");
        FileNetDocument.nameProperty = nameProperty;
        propertyFilter = new PropertyFilter();
        List<String> props = Arrays.asList("Id", "FoldersFiledIn");
        props = new ArrayList<>(props);
        if (nameProperty != null && !props.contains(nameProperty)) {
            logger.trace("added " + nameProperty);
            props.add(nameProperty);
        }
        for (String s : props) {
            propertyFilter.addIncludeProperty(new FilterElement(null, null, null, s, null));
        }
    }

    @Override
    public PropertyFilter getPropertyFilter() {
        return propertyFilter;
    }

    @Override
    public com.filenet.api.core.Containable fetchInstance() {
        return Factory.Document.fetchInstance(objectStore, id, propertyFilter);
    }

    @Override
    public com.filenet.api.core.Containable createInstance() {
        return Factory.Document.createInstance(objectStore, "Document", id);
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
            return new FileNetFolder(ceParent.get_Id(), objectStore, cache);
        } else {
            return null;
        }

    }

    @Override
    public void setParent(Folder parent) {
        com.filenet.api.core.Folder ceFolder = ((FileNetFolder)parent).ceFolder();
        ReferentialContainmentRelationship rcr =
                ceFolder.file(ceDocument(), AutoUniqueName.NOT_AUTO_UNIQUE, getName(), DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
        rcr.save(RefreshMode.REFRESH);
    }

    @Override
    public String getName() {
        if (nameProperty != null) {
            return ceDocument().getProperties().getStringValue(nameProperty);
        } else {
            return getId().toString();
        }
    }
}
