package ru.imikryakov.ecm.cli.actions;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;

import java.util.List;

public class ListAction extends Action {
    ListAction() {
        super("list", "list of documents and folders in current folder");
    }

    @Override
    public void perform() {
        List<Containable> children = getHierarchy().list();
        if (children.isEmpty()) {
            println("The folder " + getHierarchy().getCurrentFolder().getName() + " is empty");
            return;
        }

        println("Contents of folder " + getHierarchy().getCurrentFolder().getName() + ":\n");

        children.sort(getHierarchy().getComparator());

        for (Containable c : children) {
            if (c instanceof Folder)
                println("[" + c.getName() + "]");
            else
                println(c.getName());
        }
    }
}
