package ru.limedev.notes.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ru.limedev.notes.model.parcels.NotesListItem;

import static ru.limedev.notes.model.Constants.DB_RETURN_ERROR;
import static ru.limedev.notes.model.db.NoteReaderContract.NoteEntry;

public class NoteDbManager {

    private static volatile NoteDbManager instance;
    private static volatile NoteReaderDbHelper dbHelper;
    private static final String DESC = " DESC";
    private static final String EQUALS = " = ?";

    private NoteDbManager() {}

    private NoteDbManager(Context context) {
        if (context != null) {
            dbHelper = new NoteReaderDbHelper(context);
        }
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

    public static synchronized long insertValuesToDb(String name, String text,
                                                     String date, String time,
                                                     int notificationId, double ltd, double lgd) {
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NoteEntry.COLUMN_NAME_NAME, name);
            values.put(NoteEntry.COLUMN_NAME_TEXT, text);
            values.put(NoteEntry.COLUMN_NAME_DATE, date);
            values.put(NoteEntry.COLUMN_NAME_TIME, time);
            values.put(NoteEntry.COLUMN_NAME_NOTIFICATION_ID, notificationId);
            values.put(NoteEntry.COLUMN_NAME_GEO_LTD, ltd);
            values.put(NoteEntry.COLUMN_NAME_GEO_LGD, lgd);
            return db.insert(NoteEntry.TABLE_NAME, null, values);
        } else {
            return DB_RETURN_ERROR;
        }
    }

    public static synchronized void loadValuesFromDb(
            List<NotesListItem> listItems) {
        if (dbHelper != null) {
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
                    NoteEntry.COLUMN_NAME_TIME,
                    NoteEntry.COLUMN_NAME_NOTIFICATION_ID,
                    NoteEntry.COLUMN_NAME_GEO_LTD,
                    NoteEntry.COLUMN_NAME_GEO_LGD
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
                int itemNotificationId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_NOTIFICATION_ID));
                double itemLtd = cursor.getDouble(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_GEO_LTD));
                double itemLgd = cursor.getDouble(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_GEO_LGD));
                listItems.add(new NotesListItem(itemId, itemName, itemText, itemDate,
                        itemTime, itemNotificationId, itemLtd, itemLgd));
            }
            cursor.close();
        }
    }

    public static synchronized boolean removeValuesFromDb(long id) {
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String selection = NoteEntry._ID + EQUALS;
            String[] selectionArgs = {id + ""};
            int deletedRows = db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
            return deletedRows > 0;
        } else {
            return false;
        }
    }

    public static synchronized void closeDbHelper() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        instance = null;
    }
}
