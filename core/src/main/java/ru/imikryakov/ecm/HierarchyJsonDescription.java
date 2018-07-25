package ru.imikryakov.ecm;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class HierarchyJsonDescription {
    public static JsonObject getJsonObject(FolderHierarchy hierarchy) {
        return Json.createObjectBuilder().add("folder", createJsonFolder(hierarchy.getRootFolder())).build();
    }

    private static JsonObject createJsonFolder(Folder folder) {
        JsonArrayBuilder containablesBuilder = Json.createArrayBuilder();

        for (Containable c : folder.getChildren()) {
            if (c instanceof Document) {
                containablesBuilder.add(createJsonDocument((Document)c));
            }
            if (c instanceof Folder) {
                containablesBuilder.add(createJsonFolder((Folder)c));
            }
        }

        return Json.createObjectBuilder()
                .add("name", folder.getName())
                .add("containables", containablesBuilder.build())
                .build();
    }

    private static JsonObject createJsonDocument(Document document) {
        return Json.createObjectBuilder()
                .add("name", document.getName())
                .build();
    }
}
