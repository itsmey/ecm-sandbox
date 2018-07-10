package ru.imikryakov.ecm.cli;

import ru.imikryakov.ecm.Hierarchy;
import ru.imikryakov.ecm.cli.actions.Action;

import java.io.PrintStream;
import java.util.Scanner;

public class Launcher {
    private static final PrintStream OUTPUT = System.out;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Action.createActions();
        Hierarchy hierarchy = new Hierarchy();

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
            OUTPUT.print("Enter command: ");
            String command = SCANNER.next();
            action = Action.get(command);
            if (action == null) {
                OUTPUT.println("No such command.");
            } else {
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
}
