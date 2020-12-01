package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleCourseAdapter extends ArrayAdapter<Course> {

    public ScheduleCourseAdapter(Context c, ArrayList<Course> courses){
        super(c, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Course course = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_sched_course_row,
                    parent, false);
        }

        TextView courseName = (TextView) convertView.findViewById(R.id.sched_courserow_name);
        TextView courseTime = (TextView) convertView.findViewById(R.id.sched_courserow_time);
        TextView courseRoom = (TextView) convertView.findViewById(R.id.sched_courserow_room);

        courseName.setText(course.className);

        //FIXME: IS IT OKAY TO CHANGE MIN API TO 26?????
        List<String> schedSplit = Arrays.asList(course.scheduleStr.split(" "));
        String times = "";
        for(int i = 1; i < schedSplit.size(); i++){
            times = times + schedSplit.get(i) + " ";
        }
        courseTime.setText("Time: " + times);

        if(course.classroom.length() > 0){
            courseRoom.setText("Room: " + course.classroom);
            courseRoom.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
