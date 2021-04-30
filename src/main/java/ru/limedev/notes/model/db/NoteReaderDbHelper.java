package ru.limedev.notes.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ru.limedev.notes.model.db.NoteReaderContract.*;

public class NoteReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NoteReader.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                    NoteEntry._ID + " INTEGER PRIMARY KEY," +
                    NoteEntry.COLUMN_NAME_NAME + " TEXT," +
                    NoteEntry.COLUMN_NAME_TEXT + " TEXT," +
                    NoteEntry.COLUMN_NAME_DATE + " NUMERIC," +
                    NoteEntry.COLUMN_NAME_TIME + " NUMERIC)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME;

    public NoteReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
