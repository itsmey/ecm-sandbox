package ru.imikryakov.ecm.impl.sqlite;

import ru.imikryakov.ecm.impl.simple.SimpleHierarchyFactory;
import ru.imikryakov.ecm.types.FolderHierarchy;

public class SqliteHierarchyFactory extends SimpleHierarchyFactory {
    @Override
    public FolderHierarchy createEmpty() {
        return null;
    }
}
