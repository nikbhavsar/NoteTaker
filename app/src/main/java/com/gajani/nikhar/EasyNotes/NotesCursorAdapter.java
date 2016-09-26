package com.gajani.nikhar.EasyNotes;

import android.content.Context;
import android.database.Cursor;
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
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
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

        ImageView imageView = (ImageView) view.findViewById(R.id.imageDocIcon);
        if (priority == 1) {
            imageView.setImageResource(R.drawable.clipyes);
        } else {
            imageView.setImageResource(R.drawable.clip_nop);
        }

        switch (typeOfNote){

            case 0:

                l1.setBackgroundColor(view.getResources().getColor(R.color.lightPrimaryColor));

                break;
            case 1:
              /*  if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    l1.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.social1));
                } else {
                    l1.setBackground(view.getResources().getDrawable(R.drawable.social1));
                }*/
                l1.setBackgroundColor(view.getResources().getColor(R.color.social));


                break;
            case 2:
             /*   if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    l1.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.personal));
                } else {
                    l1.setBackground(view.getResources().getDrawable(R.drawable.personal));
                }*/
                l1.setBackgroundColor(view.getResources().getColor(R.color.personal));
                break;

        }

    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }


}
