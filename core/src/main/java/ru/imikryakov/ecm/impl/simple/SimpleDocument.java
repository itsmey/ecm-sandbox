package ru.imikryakov.ecm.impl.simple;

import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;

class SimpleDocument extends SimpleEcmObject implements Document {
    private Folder parent;

    SimpleDocument(String name) {
        super(name);
    }

    @Override
    public Folder getParent() {
        return parent;
    }

    @Override
    public void setParent(Folder parent) {
        this.parent = parent;
    }
}
