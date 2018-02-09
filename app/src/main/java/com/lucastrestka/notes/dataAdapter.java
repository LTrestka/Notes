/*
 * Copyright (c) 2018.  Notes
 */

package com.lucastrestka.notes;

/**
 * Created by trest on 2/5/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class dataAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "EmployeesAdapter";
    private List<Data> dataList;
    private MainActivity mainAct;

    public dataAdapter(List<Data> dList, MainActivity ma) {
        this.dataList = dList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.title.setText(data.getTitle());
        holder.date_time.setText(data.getTimeStamp());
        holder.content.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}