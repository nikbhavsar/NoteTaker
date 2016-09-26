package com.gajani.nikhar.EasyNotes;

import android.app.Activity;
import android.content.ContentValues;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int INTENT_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    CircleButton add;
    ListView listView;
    int priority = 1;
    Context c;
    int selectionPosition, checkForRestartLoader;
    Spinner spinner_nav;
    String[] categories = new String[]{"All", "Priority"};
    Context context = this;
    TextView error; 
    boolean isSame;
    String emailID,password_note,notenuTitle,passNiHint;
    private static final String FIRST_LAUNCH = "first_launch";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(android.R.id.list);

        cursorAdapter = new NotesCursorAdapter(this, null, 0);

        listView.setAdapter(cursorAdapter);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              final  Intent intent = new Intent(MainActivity.this, Editor.class);
               final Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                String noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
                Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
                cursor.moveToFirst();
               notenuTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TITLE));
               password_note = cursor.getString(cursor.getColumnIndex(DBOpenHelper.K_PASSWORD));
                passNiHint = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HINT_PASS));

                if (password_note != null) {

                    LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                    View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setView(promptView);
                    final TextView mainTitla = (TextView) promptView.findViewById(R.id.textView);
                    mainTitla.setText("Please Enter Password");
                    final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
                    final TextView hintTitla = (TextView) promptView.findViewById(R.id.passHint);
                    hintTitla.setText(passNiHint);

                    TextView forget_pass = (TextView) promptView.findViewById(R.id.errorMsg);
                    forget_pass.setText("Forget Password");
                    forget_pass.setTextColor(Color.BLUE);
                    forget_pass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                            builder2.setTitle("Do you want to send your password on you Email? " );
                            builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            builder2.show();
                        }
                    });


                    // setup a dialog window
                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                          String enterdPassword = editText.getText().toString();
                                    if (enterdPassword.equals(password_note)) {
                                        intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                                        startActivityForResult(intent, INTENT_REQUEST_CODE);
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Please Enter Valid Password",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create an alert dialog
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();

                } else {
                    intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                    startActivityForResult(intent, INTENT_REQUEST_CODE);
                }

            }
        });
        getLoaderManager().initLoader(0, null, this);
        add = (CircleButton) findViewById(R.id.circle);
        openshowCase(1000);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        selectionPosition = 0;
                        restartLoader();
                        break;
                    case 1:
                        selectionPosition = 1;
                        restartLoader();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.delete_all) {
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

                            getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);
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





    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void insertNote(String Note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, Note);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (selectionPosition == 0) {
            checkForRestartLoader = 2;
            return new CursorLoader(this, NotesProvider.CONTENT_URI, null, null, null, null);
        } else if (selectionPosition == 1) {
            checkForRestartLoader = 1;
            String selection = DBOpenHelper.PRIORITY + "=1";
            return new CursorLoader(this, NotesProvider.CONTENT_URI, null, selection, null, null);

        } else {
            checkForRestartLoader = 0;
            return new CursorLoader(this, NotesProvider.CONTENT_URI, null, null, null, null);

        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }

    public void openshowCase(int delay) {
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
        Intent intent = new Intent(this, Editor.class);
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE && resultCode == RESULT_OK || selectionPosition != 2) {
            restartLoader();

        }
    }


}
