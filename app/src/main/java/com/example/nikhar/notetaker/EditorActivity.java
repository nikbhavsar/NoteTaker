package com.example.nikhar.notetaker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editText;
    private String noteFilter,OldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editText = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();
      Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if(uri == null){
            action = intent.ACTION_INSERT;
            setTitle("New Note");
        } else {

            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID +"="+ uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri,DBOpenHelper.ALL_COLUMNS,noteFilter,null,null);
            cursor.moveToFirst();
            OldText =  cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editText.setText(OldText);
            editText.requestFocus();

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
        }

        return true;
    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,noteFilter,null);
        setResult(RESULT_OK);
        finish();

    }

    private void finishEditing(){
        String newString = editText.getText().toString().trim();
        switch(action){
            case Intent.ACTION_INSERT :
            if(newString.length()==0){
                setResult(RESULT_CANCELED);
            }else {

                insertNote(newString);
            }
                break;
            case Intent.ACTION_EDIT :
                if(newString.length() == 0){
                    deleteNote();
                }
                else if(OldText.equals(newString)){
                    setResult(RESULT_CANCELED);
                }else{
                    updateNote(newString);
                }

        }
        finish();
    }

    private void updateNote(String Note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,Note);
        getContentResolver().update(NotesProvider.CONTENT_URI,values,noteFilter,null);
        Toast.makeText(this,"Note Updated",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String Note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,Note);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
