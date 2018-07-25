package ru.imikryakov.ecm.types;

import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.HierarchyJsonDescription;
import ru.imikryakov.ecm.HierarchyXmlDescription;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;

public interface FolderHierarchy {
    Folder getRootFolder();
    void setRootAsCurrent();
    Folder getCurrentFolder();
    void setCurrentFolder(Folder f);
    String getCurrentPath();
    List<Containable> list();
    void goToFolder(String name);
    void up();
    Document createDocument(String name);
    Document createDocument(String name, Folder parent);
    Folder createFolder(String name);
    Folder createFolder(String name, Folder parent);
    void close();

    default Comparator<Containable> getComparator() {
        return (o1, o2) -> {
            if (o1 instanceof Folder && o2 instanceof Document)
                return -1;
            else if (o1 instanceof Document && o2 instanceof Folder)
                return 1;
            else
                return o1.getName().compareTo(o2.getName());
        };
    }

    default void exportToXML(String filename, Logger logger) {
        try {
            HierarchyXmlDescription description = new HierarchyXmlDescription(this);
            JAXBContext jc = JAXBContext.newInstance(HierarchyXmlDescription.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(description, new File(filename));
        } catch (JAXBException e) {
            logger.error(e);
        }
    }

    default void exportToJson(Writer writer, Logger logger) {
        JsonWriter jWriter = Json.createWriter(writer);
        JsonObject jHierarchy = HierarchyJsonDescription.getJsonObject(this);
        jWriter.writeObject(jHierarchy);
    }
}
