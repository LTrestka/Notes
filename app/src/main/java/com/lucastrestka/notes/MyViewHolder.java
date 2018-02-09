/*
 * Copyright (c) 2018.  Notes
 */

package com.lucastrestka.notes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by trest on 2/5/2018.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date_time;
    public TextView content;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.listTitle);
        date_time = (TextView) view.findViewById(R.id.lastSaved);
        content = (TextView) view.findViewById(R.id.listContent);
    }

}

