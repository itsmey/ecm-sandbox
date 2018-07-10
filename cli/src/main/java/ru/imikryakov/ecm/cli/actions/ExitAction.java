package ru.imikryakov.ecm.cli.actions;

public class ExitAction extends Action {
    public ExitAction() {
        super("exit", "exit command-line interface");
    }

    @Override
    public void perform() {
        canClose = true;
    }
}
