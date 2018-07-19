package ru.imikryakov.ecm.impl.simple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.HierarchyXmlDescription;
import ru.imikryakov.ecm.types.FolderHierarchyFactory;
import ru.imikryakov.ecm.HierarchyRandomizer;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SimpleHierarchyFactory implements FolderHierarchyFactory {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public FolderHierarchy createEmpty() {
        return new SimpleHierarchy();
    }

    @Override
    public FolderHierarchy createRandomized() {
        FolderHierarchy hierarchy = createEmpty();
        HierarchyRandomizer.populate(hierarchy);
        return hierarchy;
    }

    @Override
    public FolderHierarchy createFromXML(String filename) {
        try {
            JAXBContext jc = JAXBContext.newInstance(HierarchyXmlDescription.class);
            Unmarshaller u = jc.createUnmarshaller();
            HierarchyXmlDescription description = (HierarchyXmlDescription)u.unmarshal(new FileInputStream(filename));
            return description.createHierarchy();
        } catch (JAXBException | FileNotFoundException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public FolderHierarchy createExisting() {
        throw new RuntimeException("This creation method is not applicable for current configuration!");
    }
}
