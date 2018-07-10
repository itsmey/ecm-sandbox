package ru.imikryakov.ecm.cli.actions;

public class HelpAction extends Action {
    public HelpAction() {
        super("help", "print list of commands");
    }

    @Override
    public void perform() {
        for (Action action : Action.map().values()) {
            format("%s: %s\n", action.getId(), action.getDescription());
        }
    }
}
