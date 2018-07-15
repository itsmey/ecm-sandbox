package ru.imikryakov.ecm.impl.simple;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;

import java.util.ArrayList;
import java.util.List;

class SimpleFolder extends SimpleEcmObject implements Folder {
    private Folder parent;
    private List<Containable> children = new ArrayList<>();

    static Folder createRoot() {
        return new SimpleFolder("Root");
    }

    SimpleFolder(String name) {
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

    @Override
    public List<Containable> getChildren() {
        return children;
    }

    @Override
    public Containable addChild(Containable child) {
        children.add(child);
        child.setParent(this);
        return child;
    }
}
