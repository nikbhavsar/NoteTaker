package com.gajani.nikhar.EasyNotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhar on 4/26/2016.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    //Constants for db name and version
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_CREATED = "noteCreated";
    public static final String NOTE_TITLE = "title";
    public static final String PRIORITY = "priority";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final String ALARM_ID = "alarm_id";

    public static final String[] ALL_COLUMNS = {NOTE_ID, NOTE_TEXT, NOTE_CREATED, NOTE_TITLE, PRIORITY, YEAR, MONTH, DAY, HOUR, MINUTE, ALARM_ID};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP," + NOTE_TITLE + " TEXT, " + PRIORITY + " INTEGER DEFAULT 0, "
                    + YEAR + " INTEGER, " + MONTH + " INTEGER, " + DAY + " INTEGER, " + MINUTE + " INTEGER, " + HOUR + " INTEGER, " +
                    ALARM_ID + " INTEGER" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);

    }

}
