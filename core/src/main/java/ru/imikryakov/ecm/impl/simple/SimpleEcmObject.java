package ru.imikryakov.ecm.impl.simple;

import ru.imikryakov.ecm.types.EcmObject;

import java.util.HashMap;
import java.util.Map;

abstract class SimpleEcmObject implements EcmObject {
    String name;
    Map<String, Object> properties = new HashMap<>();

    SimpleEcmObject(String name) {
        this.name = name;
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
