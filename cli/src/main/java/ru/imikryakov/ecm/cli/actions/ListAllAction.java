package ru.imikryakov.ecm.cli.actions;

import ru.imikryakov.ecm.cli.HierarchyFormatter;

public class ListAllAction extends Action {
    ListAllAction() {
        super("listAll", "print the whole hierarchy tree");
    }

    @Override
    public void perform() {
        println(HierarchyFormatter.format(getHierarchy()).toString());
    }
}
