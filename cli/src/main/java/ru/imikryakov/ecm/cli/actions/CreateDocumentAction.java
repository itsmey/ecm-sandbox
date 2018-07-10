package ru.imikryakov.ecm.cli.actions;

public class CreateDocumentAction extends Action {
    CreateDocumentAction() {
        super("createDoc", "create document");
    }

    @Override
    public void perform() {
        print("Enter name of the document: ");
        getHierarchy().createDocument(next());
    }
}
