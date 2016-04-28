package com.gajani.nikhar.EasyNotes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import at.markushi.ui.CircleButton;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int INTENT_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    CircleButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cursorAdapter = new NotesCursorAdapter(this,null,0);
        ListView listView =(ListView) findViewById(android.R.id.list);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI +"/"+ id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE,uri);
                startActivityForResult(intent,INTENT_REQUEST_CODE);
            }
        });
        getLoaderManager().initLoader(0,null,this);
        add = (CircleButton) findViewById(R.id.circle);
        openshowCase(1000);

    }

     public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                    getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


         if(id== R.id.delete_all){
            deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(NotesProvider.CONTENT_URI,null,null);
                           restartLoader();
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

//    private void sampledata() {
//        insertNote("Hii This is Simple Note");
//        insertNote("Always working");
//        restartLoader();
//    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    private void insertNote(String Note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,Note);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,NotesProvider.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }
    public  void openshowCase(int delay){
        new MaterialShowcaseView.Builder(this)
                .setTarget(add)
                .setTitleText("Welcome to Easy Notes")
                .setDismissText("GOT IT")
                .setContentText("Click the Button to Add New Notes")
                .setDelay(delay) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Add Button") // provide a unique ID used to ensure it is only shown once
                .show();
    }

    public void openEditorForNewNote(View view) {
       openshowCase(0);
        Intent intent = new Intent(this,EditorActivity.class);
        startActivityForResult(intent,INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INTENT_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();

        }
    }
}