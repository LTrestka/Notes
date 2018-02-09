/*
 * Copyright (c) 2018.  Notes
 */

package com.lucastrestka.notes;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;


public class ASynchTast extends AsyncTask <Long, Integer, String> {
    private static final String TAG = "MyAsyncTask";
    private MainActivity mainActivity;
    public static boolean running = false;

    @Override
    protected String doInBackground(Long... longs) {
       // try {
         //   if (params[0] != 0) {
           //     long seconds = params[0];

             //   for (int i = 0; i < seconds; i++) {

//                    Thread.sleep(1000); // 1 second in millis

//                    publishProgress(i+1); // An AsyncTask method

  //                  Log.d(TAG, "doInBackground: Second = " + (i+1));
    //            }
      //      }
        //}catch(InterruptedException e){
          //  e.printStackTrace();
        //}
        Log.d(TAG, "doInBackground: Done - returning timestamp");
        return null;
    }
}
