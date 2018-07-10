package ru.imikryakov.ecm;

import java.util.HashMap;
import java.util.Map;

public abstract class EcmObject {
    private String name;
    private Map<String, Object> properties;

    public EcmObject(String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public EcmObject(String name) {
        this(name, new HashMap<String, Object>());
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
