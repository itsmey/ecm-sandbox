package ru.imikryakov.ecm.cli;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.util.List;

public class HierarchyFormatter {
    public static StringBuilder format(FolderHierarchy hierarchy) {
        Folder cf = hierarchy.getCurrentFolder();
        hierarchy.setRootAsCurrent();

        String indent = "";
        StringBuilder sb = new StringBuilder();

        addCurrentFolder(hierarchy, indent, sb);

        hierarchy.setCurrentFolder(cf);

        return sb;
    }

    private static void addCurrentFolder(FolderHierarchy hierarchy, String indent, StringBuilder sb) {
        sb.append(indent).append("[").append(hierarchy.getCurrentFolder().getName()).append("]").append("\n");
        List<Containable> children = hierarchy.getCurrentFolder().getChildren();
        children.sort(hierarchy.getComparator());
        for (Containable c : children) {
            if (c instanceof Document) {
                sb.append(indent + "  ").append(c.getName()).append("\n");
            }
            if (c instanceof Folder) {

                hierarchy.goToFolder(c.getName());
                addCurrentFolder(hierarchy, indent + "  ", sb);
                hierarchy.up();
            }
        }
    }
}
