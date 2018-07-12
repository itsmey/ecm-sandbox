package ru.imikryakov.ecm.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.FolderHierarchyFactory;
import ru.imikryakov.ecm.FolderHierarchyManager;
import ru.imikryakov.ecm.HierarchyRandomizer;
import ru.imikryakov.ecm.cli.actions.Action;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.io.PrintStream;
import java.util.Scanner;

public class Launcher {
    private static final Logger logger = LogManager.getLogger(Launcher.class.getName());
    private static final PrintStream OUTPUT = System.out;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static FolderHierarchy hierarchy = FolderHierarchyManager.SIMPLE.createRandomized();

    public static void main(String[] args) {
        Action.createActions();

        try {
            OUTPUT.println("Sandbox ECM Command-line interface\n");
            loop();
        } finally {
            SCANNER.close();
        }
    }

    private static void loop() {
        Action action;
        do {
            OUTPUT.print("\nEnter command: ");
            String command = SCANNER.next();
            OUTPUT.println();
            action = Action.get(command);
            if (action == null) {
                OUTPUT.println("No such command.");
            } else {
                logger.trace("performing action " + action.getId());
                action.perform();
            }
        } while (action == null || !action.isCanClose());
    }

    public static PrintStream getOutput() {
        return OUTPUT;
    }

    public static Scanner getScanner() {
        return SCANNER;
    }

    public static FolderHierarchy getHierarchy() {
        return hierarchy;
    }

    public static void setHierarchy(FolderHierarchy hierarchy) {
        Launcher.hierarchy = hierarchy;
    }
}
