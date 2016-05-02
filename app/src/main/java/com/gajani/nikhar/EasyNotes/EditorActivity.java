package com.gajani.nikhar.EasyNotes;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText title;
    LineEditText editText;
    ImageView imageView;
    private String noteFilter,OldText,oldTitle,newTitle,newString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editText = (LineEditText) findViewById(R.id.editText);
        title = (EditText) findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/imrock.ttf");
        editText.setTypeface(typeface);
        imageView = (ImageView) findViewById(R.id.edit_note);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setEnabled(true);
                editText.setEnabled(true);
                imageView.setVisibility(View.INVISIBLE);
            }
        });
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if(uri == null){
            action = intent.ACTION_INSERT;
            setTitle("New Note");
            title.setEnabled(true);
            editText.setEnabled(true);
            imageView.setVisibility(View.INVISIBLE);
        } else {

            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID +"="+ uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri,DBOpenHelper.ALL_COLUMNS,noteFilter,null,null);
            cursor.moveToFirst();
            OldText =  cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editText.setText(OldText);
            editText.requestFocus();
            oldTitle =  cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TITLE));
            title.setText(oldTitle);
            title.requestFocus();
            setTitle("Edit Notes");

        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_editor1, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case android.R.id.home :
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.done:
                finishEditing();
                break;

            case R.id.delete :
                deleteNote();
                break;
            case R.id.share :
                EditText ttt = (EditText) findViewById(R.id.title);
                EditText text = (EditText) findViewById(R.id.editText);
                String txt = text.getText().toString().trim();
                String tttt = ttt.getText().toString().trim();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,tttt +"\n"+ txt);
                startActivity(Intent.createChooser(sharingIntent,"Share using"));
                break;
        }

        return true;
    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,noteFilter,null);
        setResult(RESULT_OK);
        finish();

    }

    private void finishEditing(){
        newString = editText.getText().toString().trim();
        newTitle = title.getText().toString().trim();
        switch(action){
            case Intent.ACTION_INSERT :

                if(newString.length()==0 && newTitle.length()==0){
                    setResult(RESULT_CANCELED);
                }else
                {

                    insertNote(newString,newTitle);
                }

                break;
            case Intent.ACTION_EDIT :
                if(newString.length() == 0){
                    deleteNote();
                }
                else if(OldText.equals(newString) && oldTitle.equals(newTitle)){
                    setResult(RESULT_CANCELED);
                }else{
                    updateNote(newString,newTitle);
                }

        }
        finish();
    }

    private void updateNote(String Note,String Title) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,Note);
        values.put(DBOpenHelper.NOTE_TITLE,Title);
        getContentResolver().update(NotesProvider.CONTENT_URI,values,noteFilter,null);
        Toast.makeText(this,"Note Updated",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String Note,String Title) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,Note);
        values.put(DBOpenHelper.NOTE_TITLE,Title);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,values);
        setResult(RESULT_OK);
    }


    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
