package ru.limedev.notes.model.db;

import android.provider.BaseColumns;

public class NoteReaderContract {

    private NoteReaderContract() {}

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_NOTIFICATION_ID = "notification";
        public static final String COLUMN_NAME_GEO_LTD = "geo_ltd";
        public static final String COLUMN_NAME_GEO_LGD = "geo_lgd";
    }
}

