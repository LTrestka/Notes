/*
 * Copyright (c) 2018.  Notes
 */

package com.lucastrestka.notes;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class ASynchTast extends AsyncTask <Void, Void, Void> {
    private static final String TAG = "MyAsyncTask";
    private MainActivity mainActivity;
    private List<Data> dlist = new ArrayList<Data>();
    public static boolean running = false;

    public ASynchTast (MainActivity ma){
        mainActivity = ma;
    }



    @Override
    protected Void doInBackground(Void... Void) {
        running = true;
        this.dlist = mainActivity.loadList();
        Log.d(TAG, "doInBackground: Done - returning timestamp");
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        // This method is almost always used
        super.onPostExecute(v);
        mainActivity.dataList = this.dlist;
        mainActivity.whenAsynchDone();
        Log.d(TAG, "onPostExecute: loaded");
        running = false;
    }
}
