package ru.imikryakov.ecm.cli.actions;

public class UpAction extends Action {
    UpAction() {
        super("up", "go to parent folder");
    }

    @Override
    public void perform() {
        getHierarchy().up();
    }
}
