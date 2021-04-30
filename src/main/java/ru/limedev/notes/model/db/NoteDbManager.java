package ru.limedev.notes.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.limedev.notes.model.beans.NotesListItem;
import static ru.limedev.notes.model.db.NoteReaderContract.NoteEntry;

public class NoteDbManager {

    private static volatile NoteDbManager instance;
    private static volatile NoteReaderDbHelper dbHelper;
    private static final String DESC = " DESC";
    private static final String EQUALS = " = ?";

    private NoteDbManager() {}

    private NoteDbManager(Context context) {
        dbHelper = new NoteReaderDbHelper(context);
    }

    public static void getInstance(Context context) {
        if (instance == null) {
            synchronized (NoteDbManager.class) {
                if (instance == null) {
                    instance = new NoteDbManager(context);
                }
            }
        }
    }

    public static synchronized long insertValuesToDb(
            String name, String text, String date, String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_NAME, name);
        values.put(NoteEntry.COLUMN_NAME_TEXT, text);
        values.put(NoteEntry.COLUMN_NAME_DATE, date);
        values.put(NoteEntry.COLUMN_NAME_TIME, time);
        return db.insert(NoteEntry.TABLE_NAME, null, values);
    }

    public static synchronized @NonNull List<NotesListItem> loadValuesFromDb(
            List<NotesListItem> listItems) {
        if (listItems == null) {
            listItems = new ArrayList<>();
        } else {
            listItems.clear();
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                NoteEntry.COLUMN_NAME_NAME,
                NoteEntry.COLUMN_NAME_TEXT,
                NoteEntry.COLUMN_NAME_DATE,
                NoteEntry.COLUMN_NAME_TIME
        };
        String sortOrder = NoteReaderContract.NoteEntry.COLUMN_NAME_TEXT + DESC;
        Cursor cursor = db.query(
                NoteReaderContract.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(NoteEntry._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_NAME));
            String itemText = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TEXT));
            String itemDate = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_DATE));
            String itemTime = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TIME));
            listItems.add(new NotesListItem(itemId, itemName, itemText, itemDate, itemTime));
        }
        cursor.close();
        return listItems;
    }

    public static synchronized boolean removeValuesFromDb(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = NoteEntry._ID + EQUALS;
        String[] selectionArgs = {id + ""};
        int deletedRows = db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    public static synchronized void closeDbHelper() {
        dbHelper.close();
        instance = null;
    }
}
