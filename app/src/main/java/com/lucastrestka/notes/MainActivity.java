package com.lucastrestka.notes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.lucastrestka.notes.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private Menu m;
    private RecyclerView recyclerView;
    private dataAdapter mAdapter;
    public List<Data> dataList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    int Flag;


    public void doAsynchLoad(){
        new ASynchTast(this).execute();
    }
    public void whenAsynchDone(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new dataAdapter(dataList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doAsynchLoad();
        Log.d(TAG, "onCreate: main");
    }


    @Override
    protected void onResume() { // Resets recycler view for updates.
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new dataAdapter(dataList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        super.onResume();
    }


    @Override
    protected void onPause() {
        saveArray(dataList);
        super.onPause();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
                                    // Send user to edit existing note in Note activity
                                    // Awaits result.
        int pos = recyclerView.getChildLayoutPosition(v);
        Data d = dataList.get(pos);
        Flag = pos;
        Intent intent = new Intent(this, Note.class);
        intent.putExtra("DATA_PASSED", dataList.get(pos));
        Log.d(TAG, "onClick: passing object from main");
        startActivityForResult(intent, 100);
        return;
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
                                            // Creates prompt to delete note on long click.
        final int pos = recyclerView.getChildLayoutPosition(v);
        Data d = dataList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_note_add_white_48dp);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dataList.remove(pos);
                onResume();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setTitle("Delete Note");
        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu r) {    // Creates menu, and sets
        getMenuInflater().inflate(R.menu.menu, r);  // Proper menu items
        m = r;                                      // For main activity page
        m.findItem(R.id.addNote).setVisible(true);
        m.findItem(R.id.infoButton).setVisible(true);
        m.findItem(R.id.saveNote).setVisible(false);
        return true;
    }

    @Override   // If Save function returns result 111, adds data object to data array.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (resultCode == 101){ // Replaces data object inside of existing note.
                dataList.remove(Flag);
                dataList.add(0, (Data) data.getSerializableExtra("Changed Data"));
                onResume();
            }
        }
        if(requestCode == 121){
            if(resultCode == 111){  // creates new object into dataList
                dataList.add(0,(Data) data.getSerializableExtra("Data"));
                onResume();
            }
        }
    }

    @Override   // Functions that the menu buttons run
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNote:  // Opens note activity for user to create a new note
                                // Receives data object if requirements are met.
                Intent intent = new Intent(this, Note.class);
                startActivityForResult(intent, 121);
                return true;
            case R.id.infoButton:   // Opens App info page
                Intent infotent = new Intent(this, info.class);
                startActivity(infotent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void saveArray(List<Data> dlist){
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoder)));
            writer.setIndent(" ");
            writer.beginArray();
            for(Data d: dlist){
                saveListObject(writer, d);
            }
            writer.endArray();
            writer.close();
        }catch (Exception e) {
            e.getStackTrace();
        }
    }


    public void saveListObject(JsonWriter writer, Data data) throws IOException{
        try {
            writer.beginObject();
            writer.name("dateTime").value(data.getTimeStamp());
            writer.name("savedTitle").value(data.getTitle());
            writer.name("savedContent").value(data.getContent());
            writer.endObject();
            Log.d(TAG, "saveArray: Saved an object");
        } catch (Exception e){
            e.getStackTrace();
        }
    }
    public List<Data> loadList(){   // Loads array
        List<Data> dlist = new ArrayList<Data>();
        try{
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader read = new JsonReader(new InputStreamReader(is, getString(R.string.encoder)));
            read.beginArray();
            while (read.hasNext()){     // Adds a data object to list by calling saved Data object
                dlist.add(loadListObject(read));
            }

        }catch (Exception e){
            e.getStackTrace();
        }

        return dlist;
    }

    public Data loadListObject(JsonReader reader){  // takes an individual object from saved array
        Data object = new Data();                   // Creates default Data object and sets values
                                                    // Based on what is saved, then returns it into
                                                    // dataList object in loadList method.
        try{
            reader.beginObject();
            while(reader.hasNext()){
                String text = reader.nextName();
                if(text.equals("dateTime")){
                    object.setTimesStamp(reader.nextString());
                }
                else if(text.equals("savedTitle")){
                    object.setTitle(reader.nextString());
                }
                else if(text.equals("savedContent")){
                    object.setContent(reader.nextString());
                }
                else{
                    reader.skipValue();
                }
            }
            reader.endObject();

        }catch (Exception e){
            e.getStackTrace();
        }
        return object;
    }
}
