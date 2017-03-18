package com.gajani.nikhar.EasyNotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Nikhar on 4/26/2016.
 */
public class NotesCursorAdapter extends CursorAdapter implements Filterable {
    private Context context;

    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listdemo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TITLE));
        String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

        LinearLayout l1 = (LinearLayout) view.findViewById(R.id.layout_list);
        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        TextView note_tv = (TextView) view.findViewById(R.id.note_Text);

        int priority = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.PRIORITY));
        String hasPass  = cursor.getString(cursor.getColumnIndex(DBOpenHelper.K_PASSWORD));
        int typeOfNote = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.NOTE_TYPE));
        final ImageView protect = (ImageView) view.findViewById(R.id.lock);
        if(hasPass != null){

            protect.setVisibility(View.VISIBLE);
            tv.setText(noteTitle);
            note_tv.setText("***************");

        } else {

            protect.setVisibility(View.INVISIBLE);
            tv.setText(noteTitle);
            note_tv.setText(noteText);

        }

        ImageView type = (ImageView) view.findViewById(R.id.typeOfnote);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.listmainLayout);

        if (priority == 1) {
            layout.setBackgroundResource(R.drawable.edittext2);
        } else {
            layout.setBackgroundResource(R.drawable.edittext_bg);
        }

        switch (typeOfNote){

            case 0:

              type.setBackgroundResource(R.drawable.button);

                break;
            case 1:

                type.setBackgroundResource(R.drawable.button1);

                break;
            case 2:

                type.setBackgroundResource(R.drawable.button2);

                break;
        }

    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }


}
