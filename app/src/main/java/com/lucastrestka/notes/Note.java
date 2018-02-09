package com.lucastrestka.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lucastrestka.notes.MainActivity;

import com.lucastrestka.notes.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note extends AppCompatActivity {

    private Menu m;
    private EditText title;
    private EditText content;
    private String Date_Time, title_passed, content_passed;
    private Data d;
    private int Flag; // used to determine which action to take on save button click
    private static final String TAG = "Note";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        title = (EditText) findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: done");

        if(intent.hasExtra("DATA_PASSED")){    // If user clicks on one of the notes in main
                                                    // this takes that object and places it in the
                                                    // title/content. Strings used to determine if
                                                    // change occurs
            Data x = new Data();
            x = (Data) intent.getSerializableExtra("DATA_PASSED");
            title.setText(x.getTitle());
            content.setText(x.getContent());
            title_passed = x.getTitle();
            content_passed = x.getContent();
            Flag = 1;
        }
        else {
            Flag = 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu r) {    // recreates options menu and changes
                                                    // visibility so only save button shows.
        getMenuInflater().inflate(R.menu.menu, r);
        m = r;
        m.findItem(R.id.addNote).setVisible(false);
        m.findItem(R.id.infoButton).setVisible(false);
        m.findItem(R.id.saveNote).setVisible(true);
        return true;
    }

    public void newDataMade(View v){ // sends new Data object to dataList in main
        Intent intent = new Intent();
        intent.putExtra("Data",d);
        setResult(111, intent);
        finish();
    }

    public void changesMade(View v){    // Sends updated object to main
        Intent intent = new Intent();
        intent.putExtra("Changed Data", d);
        setResult(101,intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        // Checks to see if back button is pressed, takes actions described below

        boolean flag2 = false;  // Used to flag if title exists or existing changes were made
                                // Flag = 0 = new note
                                // Flag = 1 = existing note
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if(title.getText().toString().equals(null)||title.getText().toString().equals("")){
               Toast.makeText(this, "No Title: Note not saved", Toast.LENGTH_SHORT).show();
               finish();
               return true;
            }
            else{
                if (Flag == 0) {    // if there is anything in the title, alert will ask to save
                    flag2 = true;
                }
                else if (Flag == 1) {    // Checks to see if changes are made, prompts user to save if yes
                    if (!title.getText().toString().equals(title_passed) ||
                            !content.getText().toString().equals(content_passed)) {
                        flag2 = true;
                    }
                }
            }

            if (flag2 == true){ // If new note has a title, or existing note has changes made,
                                // alerts user to save, or continue, and Creates new, or modifies existing note,
                                // if save is chosen, exits without doing anything otherwise.

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_note_add_white_48dp);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Date_Time = DateFormat.getDateTimeInstance().format(new Date());
                        d = new Data(Date_Time, title.getText().toString(), content.getText().toString());
                        if (Flag == 0){
                            newDataMade(findViewById(R.id.saveNote));
                        }
                        if (Flag == 1){
                            changesMade(findViewById(R.id.saveNote));
                        }

                    }
                });
                builder.setNegativeButton("Exit without saving", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setMessage("Would you like to save this note?");
                builder.setTitle("Changes Detected");
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                // First checks to see if title exists
                // If title exists, checks Flag. For Flag 0, creates new object to send to main.
                // For Flag = 1, checks to see if changes are made, if yes, creates object to update main,
                // Otherwise, exits activity. Appropriate toasts vary with each option.
                if(title.getText().toString().equals(null)||title.getText().toString().equals("")) {
                    Toast.makeText(this, "No Title: Note not saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Date_Time = DateFormat.getDateTimeInstance().format(new Date());
                    d = new Data(Date_Time, title.getText().toString(), content.getText().toString());
                    if (Flag == 0) {
                        Toast.makeText(this, "New Note Saved!", Toast.LENGTH_SHORT).show();
                        newDataMade(findViewById(item.getItemId()));
                    } else if (Flag == 1) {
                        if (d.getTitle().equals("") || d.getTitle().equals(null)){
                            d.setTitle("(No Name)");
                        }
                        if (title.getText().toString().equals(title_passed) &&
                                content.getText().toString().equals(content_passed)) {
                            Toast.makeText(this, "No Changes Made", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
                            changesMade(findViewById(item.getItemId()));
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



