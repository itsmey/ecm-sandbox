package ru.imikryakov.ecm;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Document;
import ru.imikryakov.ecm.types.Folder;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.json.*;

public class HierarchyJsonDescription {
    public static JsonObject getJsonObject(FolderHierarchy hierarchy) {
        return Json.createObjectBuilder().add("folder", createJsonFolder(hierarchy.getRootFolder(), hierarchy.getCurrentFolder())).build();
    }

    private static JsonObject createJsonFolder(Folder folder, Folder currentFolder) {
        JsonArrayBuilder containablesBuilder = Json.createArrayBuilder();

        for (Containable c : folder.getChildren()) {
            if (c instanceof Document) {
                containablesBuilder.add(createJsonDocument((Document)c));
            }
            if (c instanceof Folder) {
                containablesBuilder.add(createJsonFolder((Folder)c, currentFolder));
            }
        }

        JsonObjectBuilder result = Json.createObjectBuilder()
                .add("name", folder.getName());

        if (folder.equals(currentFolder))
            result.add("current", JsonValue.TRUE);

        return result
                .add("containables", containablesBuilder.build())
                .build();
    }

    private static JsonObject createJsonDocument(Document document) {
        return Json.createObjectBuilder()
                .add("name", document.getName())
                .build();
    }
}
