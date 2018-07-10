package ru.imikryakov.ecm.cli.actions;

public class CreateFolderAction extends Action {
    CreateFolderAction() {
        super("createFolder", "create folder");
    }

    @Override
    public void perform() {
        print("Enter name of the folder: ");
        getHierarchy().createFolder(next());
    }
}
