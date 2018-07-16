package ru.imikryakov.ecm.impl.sqlite;

import ru.imikryakov.ecm.types.Containable;
import ru.imikryakov.ecm.types.Folder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteFolder extends SqliteContainable implements Folder {

    SqliteFolder(DbHelper dbHelper) {
        super(dbHelper);
    }

    SqliteFolder(String id, DbHelper dbHelper) {
        super(id, dbHelper);
    }

    @Override
    public List<Containable> getChildren() {
        try {
            List<Containable> result = new ArrayList<>();
            PreparedStatement stmt = dbHelper.getPreparedStatement("SELECT ID, TYPE FROM CONTAINABLE WHERE PARENT_ID = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int type = rs.getInt("TYPE");
                String id = rs.getString("ID");
                if (type == DbHelper.TYPE_DOCUMENT) {
                    result.add(new SqliteDocument(id, dbHelper));
                } else
                if (type == DbHelper.TYPE_FOLDER) {
                    result.add(new SqliteFolder(id, dbHelper));
                }
            }
            stmt.close();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Containable addChild(Containable child) {
        child.setParent(this);
        return child;
    }
}
