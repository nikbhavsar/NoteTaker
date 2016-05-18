package com.gajani.nikhar.EasyNotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Nikhar on 4/26/2016.
 */
public class NotesCursorAdapter extends CursorAdapter {
    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TITLE));
        String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        int pos = noteText.indexOf(10);
        if(pos != -1)
        {
            noteText = noteText.substring(0,pos)+"...";
        }
        TextView tv= (TextView) view.findViewById(R.id.tvNote);
        TextView note_tv = (TextView) view.findViewById(R.id.note_Text);
        tv.setText(noteTitle);
        note_tv.setText(noteText);
    }
}
