package ru.imikryakov.ecm.types;

import java.util.List;

public interface Folder extends Containable {
    List<Containable> getChildren();
    Containable addChild(Containable child);
}
