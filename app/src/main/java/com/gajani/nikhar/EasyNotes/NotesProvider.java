package com.gajani.nikhar.EasyNotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Nikhar on 4/26/2016.
 */
public class NotesProvider extends ContentProvider {

    private static final String AUTHORITY = "com.gajani.nikhar.EasyNotes";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int TITLE = 3;
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    public static final String CONTENT_ITEM_TYPE = "notes";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH,TITLE);
        uriMatcher.addURI(AUTHORITY,BASE_PATH,NOTES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",NOTES_ID);

    }

    private SQLiteDatabase database;
    @Override
    public boolean onCreate() {

        DBOpenHelper openHelper = new DBOpenHelper(getContext());

        database = openHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri)== NOTES_ID){
            selection = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }
        return database.query(DBOpenHelper.TABLE_NOTES,DBOpenHelper.ALL_COLUMNS,selection,null,null,null,DBOpenHelper.NOTE_CREATED +" DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long ID = database.insert(DBOpenHelper.TABLE_NOTES,null,values);
        return Uri.parse(BASE_PATH + "/" + ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES,selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES,values,selection,selectionArgs);
    }
}