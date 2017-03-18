package com.gajani.nikhar.EasyNotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhar on 4/26/2016.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    //Constants for db name and version
    private static final String DATABASE_NAME ="notes.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String USER_DATA_TABLE = "user";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_ANS1 = "user_ans1";
    public static final String USER_ANS2 = "user_ans2";
    public static final String USER_PASS = "user_pass";
    public static final String MAIN_PASS_HINT = "main_pass_hint";
    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_ID = "_id";
    public static final String DATA_ID = "_id";
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
    public static final String K_PASSWORD = "_password";
    public static final String HINT_PASS = "hint";
    public static final String NOTE_TYPE = "_type";



    public static final String[] ALL_COLUMN_USER = {DATA_ID,USER_NAME,USER_EMAIL,USER_ANS1,USER_ANS2,USER_PASS};

    public static final String[] ALL_COLUMNS = {NOTE_ID, NOTE_TEXT, NOTE_CREATED, NOTE_TITLE, PRIORITY, YEAR, MONTH, DAY, HOUR, MINUTE, ALARM_ID, K_PASSWORD,HINT_PASS,NOTE_TYPE};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP," + NOTE_TITLE + " TEXT, " + PRIORITY + " INTEGER DEFAULT 0, "
                    + YEAR + " INTEGER, " + MONTH + " INTEGER, " + DAY + " INTEGER, " + MINUTE + " INTEGER, " + HOUR + " INTEGER, " +
                    ALARM_ID + " INTEGER, " + K_PASSWORD + " TEXT, " + HINT_PASS + " TEXT, " + NOTE_TYPE + " INTEGER DEFAULT 2" +
                    ")";
    private static final String TABLE_USER_CREATE =
            "CREATE TABLE " + USER_DATA_TABLE + " (" +
                    DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_NAME + " TEXT, " + USER_EMAIL + " TEXT NOT NULL DEFAULT \'email\', " + USER_ANS1 + " TEXT, " + USER_ANS2 + " TEXT, " + MAIN_PASS_HINT + " TEXT, " +USER_PASS + " TEXT" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_USER_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + USER_DATA_TABLE);
        onCreate(db);

    }

    public void insertUserData  (String userName, String userEmail, String ans1 , String ans2 , String pass , String mainPassHint)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_ANS1, ans1);
        contentValues.put(USER_ANS2, ans2);
        contentValues.put(USER_PASS, pass);
        contentValues.put(MAIN_PASS_HINT, mainPassHint);
        db.insert(USER_DATA_TABLE, null, contentValues);

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " +USER_DATA_TABLE , null );
        return res;
    }

    public void updateUserData(Integer id ,String userName, String userEmail, String ans1 , String ans2 ,String pass , String mainPassHint)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_ANS1, ans1);
        contentValues.put(USER_ANS2, ans2);
        contentValues.put(USER_PASS, pass);
        contentValues.put(MAIN_PASS_HINT, mainPassHint);
       db.update(USER_DATA_TABLE,contentValues,"_id =" +id, null);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_DATA_TABLE);
        return numRows;
    }

    public boolean isTableExists(boolean openDb) {

        SQLiteDatabase mDatabase = this.getReadableDatabase();

        if(openDb) {
            if(mDatabase == null || !mDatabase.isOpen()) {
                mDatabase = getReadableDatabase();
            }

            if(!mDatabase.isReadOnly()) {
                mDatabase.close();
                mDatabase = getReadableDatabase();
            }
        }

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+USER_DATA_TABLE+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
