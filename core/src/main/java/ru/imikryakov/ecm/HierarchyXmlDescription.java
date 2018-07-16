package ru.imikryakov.ecm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "hierarchy")
public class HierarchyXmlDescription {
    private static Logger logger = LogManager.getLogger();

    public HierarchyXmlDescription() {}

    public HierarchyXmlDescription(FolderHierarchy hierarchy) {
        rootFolder = new FolderDescription(hierarchy.getRootFolder());
    }

    @XmlElement(name = "folder")
    private FolderDescription rootFolder;

    FolderDescription getRootFolder() {
        return rootFolder;
    }

    static class FolderDescription {
        FolderDescription() {}

        FolderDescription(Folder folder) {
            name = folder.getName();
            if (folder.getChildren().isEmpty()) {
                contents = null;
            } else {
                contents = new ArrayList();
                for (Containable c : folder.getChildren()) {
                    if (c instanceof Document) {
                        contents.add(new DocumentDescription((Document)c));
                    }
                    if (c instanceof Folder) {
                        contents.add(new FolderDescription((Folder)c));
                    }
                }
            }
        }

        @XmlAttribute(required = true)
        private String name;

        @XmlElementWrapper(name = "contents")
        @XmlElements({
                @XmlElement(name="folder", type = FolderDescription.class),
                @XmlElement(name="document", type = DocumentDescription.class)})
        private List contents;

        String getName() {
            return name;
        }

        List getContents() {
            return contents;
        }
    }

    static class DocumentDescription {
        DocumentDescription() {}

        DocumentDescription(Document document) {
            name = document.getName();
        }

        @XmlAttribute(required = true)
        private String name;

        public String getName() {
            return name;
        }
    }

    public FolderHierarchy createHierarchy() {
        FolderHierarchy hierarchy = FolderHierarchyManager.get().createEmpty();

        processContents(getRootFolder().getContents(), hierarchy);

        hierarchy.setRootAsCurrent();
        return hierarchy;
    }

    private void processContents(List contents, FolderHierarchy hierarchy) {
        for (Object c : contents) {
            if (c instanceof DocumentDescription) {
                hierarchy.createDocument(((DocumentDescription)c).getName());
            }
            if (c instanceof FolderDescription) {
                String folderName = ((FolderDescription)c).getName();
                hierarchy.createFolder(folderName);
                if (((FolderDescription)c).getContents() != null) {
                    hierarchy.goToFolder(folderName);
                    processContents(((FolderDescription) c).getContents(), hierarchy);
                    hierarchy.up();
                }
            }
        }
    }
}
