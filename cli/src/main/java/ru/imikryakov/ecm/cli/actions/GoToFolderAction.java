package ru.imikryakov.ecm.cli.actions;

public class GoToFolderAction extends Action {
    GoToFolderAction() {
        super("goto", "go to folder");
    }

    @Override
    public void perform() {
        print("Enter name of the folder: ");
        getHierarchy().goToFolder(next());
    }
}
