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

        children.sort((o1, o2) -> {
            if (o1 instanceof Folder && o2 instanceof Document)
                return -1;
            else if (o1 instanceof Document && o2 instanceof Folder)
                return 1;
            else
                return o1.getName().compareTo(o2.getName());
        });

        for (Containable c : children) {
            if (c instanceof Folder)
                println("[" + c.getName() + "]");
            else
                println(c.getName());
        }
    }
}
