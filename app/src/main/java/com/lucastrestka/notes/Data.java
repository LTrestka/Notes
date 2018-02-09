/*
 * Copyright (c) 2018.  Notes
 */

package com.lucastrestka.notes;

import java.io.Serializable;

/**
 * Created by trest on 2/05/2018.
 */

public class Data implements Serializable{

    private String dateTime;
    private String savedTitle;
    private String savedContent;

    public Data(){
        this.dateTime = "Never";
        this.savedTitle = "No Name";
        this.savedContent = "No Content";
    }
    public Data(String dt, String sT, String sC){
        this.dateTime = dt;
        this.savedTitle = sT;
        this.savedContent = sC;
    }

    public String getTitle() {
        return savedTitle;
    }

    public void setTitle(String tText) {
        this.savedTitle = tText;
    }

    public String getContent() {return savedContent; }

    public void setContent(String cText) {this.savedContent = cText; }

    public String getTimeStamp() {
        return dateTime;
    }

    public void setTimesStamp(String timestamp) {
        this.dateTime = timestamp;
    }

}
