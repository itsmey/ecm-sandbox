package ru.imikryakov.ecm.cli.actions;

import ru.imikryakov.ecm.FolderHierarchyManager;

public class FromXmlAction extends Action {
    FromXmlAction() {
        super("fromXml", "import hierarchy from XML file");
    }

    @Override
    public void perform() {
        print("Enter name of the XML file: ");
        try {
            setHierarchy(FolderHierarchyManager.get().createFromXML(next()));
        } catch (Exception e) {
            println("Action not available");
        }
    }
}
