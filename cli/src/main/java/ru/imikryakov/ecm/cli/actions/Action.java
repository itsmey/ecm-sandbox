package ru.imikryakov.ecm.cli.actions;

import ru.imikryakov.ecm.cli.Launcher;
import ru.imikryakov.ecm.types.FolderHierarchy;

import java.util.HashMap;
import java.util.Map;

public abstract class Action {
    private String id;
    private String description;
    boolean canClose;

    private static Map<String, Action> map = new HashMap<>();

    public static void createActions() {
        new HelpAction();
        new ExitAction();
        new PrintCurrentAction();
        new ListAction();
        new GoToFolderAction();
        new UpAction();
        new CreateDocumentAction();
        new CreateFolderAction();
    }

    public static Action get(String id) {
        return map.get(id);
    }

    public static Map<String, Action> map() {
        return map;
    }

    Action(String id, String description) {
        this.id = id;
        this.description = description;
        map.put(id, this);
    }

    public abstract void perform();

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCanClose() {
        return canClose;
    }

    static void println(String s) {
        Launcher.getOutput().println(s);
    }

    static void print(String s) {
        Launcher.getOutput().print(s);
    }

    static void format(String format, Object... args) {
        Launcher.getOutput().format(format, args);
    }

    static String next() {
        return Launcher.getScanner().next();
    }

    static FolderHierarchy getHierarchy() {
        return Launcher.getHierarchy();
    }
}
