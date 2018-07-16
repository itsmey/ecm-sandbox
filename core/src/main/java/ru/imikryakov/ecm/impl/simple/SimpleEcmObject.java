package ru.imikryakov.ecm.impl.simple;

import ru.imikryakov.ecm.types.EcmObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

abstract class SimpleEcmObject implements EcmObject {
    private String name;
    private Map<String, Object> properties = new HashMap<>();

    SimpleEcmObject(String name) {
        this.name = name;
        properties.put("CreatedAt", new Date());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }
}
