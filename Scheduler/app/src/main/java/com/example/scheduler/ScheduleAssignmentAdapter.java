package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ScheduleAssignmentAdapter extends ArrayAdapter<Assignment> {

    public ScheduleAssignmentAdapter(Context c, ArrayList<Assignment> assigns){
        super(c, 0, assigns);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Assignment assign = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_assignment_row,
                    parent, false);
        }

        return convertView;
    }
}
