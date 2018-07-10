package ru.imikryakov.ecm;

import java.util.List;

public interface Containable {
    List<Folder> getParents();
    void file(Folder folder);
}
