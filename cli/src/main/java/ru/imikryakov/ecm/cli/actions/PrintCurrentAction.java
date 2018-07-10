package ru.imikryakov.ecm.cli.actions;

public class PrintCurrentAction extends Action {
    PrintCurrentAction() {
        super("pwd", "print current directory");
    }

    @Override
    public void perform() {
        println(getHierarchy().getCurrentPath());
    }
}
