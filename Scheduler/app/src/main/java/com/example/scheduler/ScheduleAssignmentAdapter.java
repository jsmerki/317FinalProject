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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_sched_assign_row,
                    parent, false);
        }

        TextView assignName = (TextView) convertView.findViewById(R.id.sched_assignrow_name);
        TextView assignCourse = (TextView) convertView.findViewById(R.id.sched_assignrow_course);

        assignName.setText(assign.assignName);
        assignCourse.setText(assign.getCourse().className);

        return convertView;
    }
}
