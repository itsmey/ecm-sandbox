package ru.imikryakov.ecm.cli.actions;

public class ToXmlAction extends Action {
    ToXmlAction() {
        super("toXml", "export current state to XML file");
    }

    @Override
    public void perform() {
        print("Enter name of the XML file: ");
        getHierarchy().export(next());
    }
}
