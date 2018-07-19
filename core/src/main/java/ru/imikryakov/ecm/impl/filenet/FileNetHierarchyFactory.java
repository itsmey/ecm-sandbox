package ru.imikryakov.ecm.impl.filenet;

import ru.imikryakov.ecm.config.Config;
import ru.imikryakov.ecm.config.Properties;
import ru.imikryakov.ecm.types.FolderHierarchy;
import ru.imikryakov.ecm.types.FolderHierarchyFactory;

public class FileNetHierarchyFactory implements FolderHierarchyFactory {
    @Override
    public FolderHierarchy createEmpty() {
        throw new RuntimeException("This creation method is not applicable for current configuration!");
    }

    @Override
    public FolderHierarchy createRandomized() {
        throw new RuntimeException("This creation method is not applicable for current configuration!");
    }

    @Override
    public FolderHierarchy createFromXML(String filename) {
        throw new RuntimeException("This creation method is not applicable for current configuration!");
    }

    @Override
    public FolderHierarchy createExisting() {
        return new FileNetHierarchy(Config.getProperty(Properties.FILENET_OBJECT_STORE_NAME, true));
    }
}
