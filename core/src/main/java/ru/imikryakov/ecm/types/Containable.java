package ru.imikryakov.ecm.types;

public interface Containable extends EcmObject {
    Folder getParent();
    void setParent(Folder parent);
}
