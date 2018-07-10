package ru.imikryakov.ecm.types;

import java.util.Map;

public interface EcmObject {
    String getName();
    Map<String, Object> getProperties();
}
