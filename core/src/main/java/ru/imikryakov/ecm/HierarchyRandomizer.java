package ru.imikryakov.ecm;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.util.Random;

public class HierarchyRandomizer {
    private static final int DEPTH_MIN = 1;
    private static final int DEPTH_MAX = 5;
    private static final int DOCS_MAX = 5;
    private static final int FOLDERS_MAX = 5;

    private static int counter = 1;

    private static final Random random = new Random();

    public static void populate(FolderHierarchy hierarchy) {
        generateDocs(hierarchy);
        generateFolders(hierarchy, DEPTH_MIN + random.nextInt(DEPTH_MAX - DEPTH_MIN + 1));
    }

    private static void generateDocs(FolderHierarchy hierarchy) {
        int numDocs = random.nextInt(DOCS_MAX) + 1;
        for (int i = 0; i < numDocs; i++) {
            hierarchy.createDocument("Document " + counter);
            counter++;
        }
    }

    private static void generateFolders(FolderHierarchy hierarchy, int depth) {
        if (depth == 0) {
            return;
        }
        int numFolders = random.nextInt(FOLDERS_MAX) + 1;
        for (int i = 0; i < numFolders; i++) {
            hierarchy.createFolder("Folder " + counter);
            counter++;
        }
        for (Containable c : hierarchy.getCurrentFolder().getChildren()) {
            if (c instanceof Folder) {
                hierarchy.goToFolder(c.getName());
                generateDocs(hierarchy);
                generateFolders(hierarchy, depth - 1);
                hierarchy.up();
            }
        }
    }
}
