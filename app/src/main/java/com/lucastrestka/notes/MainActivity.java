package com.lucastrestka.notes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private List<Data> dataList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    int Flag;

//new

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new dataAdapter(dataList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            }
        }
        if(requestCode == 121){
            if(resultCode == 111){  // creates new object into dataList
                dataList.add(0,(Data) data.getSerializableExtra("Data"));
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


    @Override  // saves the datalist array in case of orientation change
    protected void onSaveInstanceState(Bundle outState) {
        int num_entries=0;
        for(int i =0;i<dataList.size();i++) {
            outState.putSerializable("Data "+i, dataList.get(i));
            num_entries += 1;
        }
        outState.putInt("NUMBER_OF_ITEMS", num_entries);
        super.onSaveInstanceState(outState);
    }

    @Override // recovers datalist in the event of orientation chage
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int num_entries = savedInstanceState.getInt("NUMBER_OF_ITEMS");
        for(int i = 0; i<num_entries;i++){
            dataList.add((Data)savedInstanceState.getSerializable("Data "+i));
        }
    }
}
