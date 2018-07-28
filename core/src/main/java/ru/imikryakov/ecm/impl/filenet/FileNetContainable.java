package ru.imikryakov.ecm.impl.filenet;

import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.util.Id;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;

import java.util.Map;
import java.util.UUID;

public abstract class FileNetContainable implements Containable {
    private static Logger logger = LogManager.getLogger();
    FileNetManager filenet;
    Id id;
    com.filenet.api.core.Containable instance;
    Map<Id, com.filenet.api.core.Containable> cache;

    FileNetContainable(FileNetManager filenet, Map<Id, com.filenet.api.core.Containable> cache) {
        this.cache = cache;
        this.filenet = filenet;
        id = new Id(UUID.randomUUID().toString());
        logger.trace("creating new Containable with id = " + id);
        instance = createInstance();
    }

    FileNetContainable(Id id, FileNetManager filenet, Map<Id, com.filenet.api.core.Containable> cache) {
        logger.trace("fetching Containable with id = " + id);
        this.cache = cache;
        this.filenet = filenet;
        this.id = id;
        instance = cache.get(id);
        if (instance == null) {
            logger.trace("not found in cache. will go to filenet");
            instance = fetchInstance();
            cache.put(instance.get_Id(), instance);
        } else {
            logger.trace("found in cache. refresh");
            filenet.refresh(ceInstance());
        }
    }

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

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileNetContainable)
            return hashCode() == obj.hashCode();
        else
            return false;
    }
}
