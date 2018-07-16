package ru.imikryakov.ecm.impl.sqlite;

import ru.imikryakov.ecm.types.Document;

class SqliteDocument extends SqliteContainable implements Document {
    SqliteDocument(DbHelper dbHelper) {
        super(dbHelper);
    }

    SqliteDocument(String id, DbHelper dbHelper) {
        super(id, dbHelper);
    }
}
