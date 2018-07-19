package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;

import java.util.Map;
import java.util.UUID;

public abstract class FileNetContainable implements Containable {
    private static Logger logger = LogManager.getLogger();
    ObjectStore objectStore;
    Id id;
    com.filenet.api.core.Containable instance;
    Map<Id, com.filenet.api.core.Containable> cache;

    FileNetContainable(ObjectStore objectStore, Map<Id, com.filenet.api.core.Containable> cache) {
        this.cache = cache;
        this.objectStore = objectStore;
        id = new Id(UUID.randomUUID().toString());
        logger.trace("creating new Containable with id = " + id);
        instance = createInstance();
    }

    FileNetContainable(Id id, ObjectStore objectStore, Map<Id, com.filenet.api.core.Containable> cache) {
        logger.trace("fetching Containable with id = " + id);
        this.cache = cache;
        this.objectStore = objectStore;
        this.id = id;
        instance = cache.get(id);
        if (instance == null) {
            logger.trace("not found in cache. will go to filenet");
            instance = fetchInstance();
            cache.put(instance.get_Id(), instance);
        } else {
            logger.trace("found in cache. refresh");
            ceInstance().refresh(getPropertyFilter());
        }
    }

    public abstract PropertyFilter getPropertyFilter();

    public abstract com.filenet.api.core.Containable fetchInstance();

    public abstract com.filenet.api.core.Containable createInstance();

    public abstract IndependentlyPersistableObject ceInstance();

    public Id getId() {
        return id;
    }

    @Override
    public String getName() {
        return instance.get_Name();
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }
}
