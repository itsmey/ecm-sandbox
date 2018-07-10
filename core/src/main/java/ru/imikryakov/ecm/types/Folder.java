package ru.imikryakov.ecm.types;

import java.util.List;

public interface Folder extends Containable {
    List<Containable> getChildren();
    void addChild(Containable child);
}
