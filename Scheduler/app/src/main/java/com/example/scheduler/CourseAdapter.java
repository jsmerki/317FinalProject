package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

public class CourseAdapter extends ArrayAdapter<Course> {

    public Context context;

    public CourseAdapter(Context c, ArrayList<Course> courses){
        super(c, 0, courses);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        Course course = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_course_row,
                    parent, false);
        }

        TextView courseName = (TextView) convertView.findViewById(R.id.course_title);
        TextView courseProf = (TextView) convertView.findViewById(R.id.course_prof);
        TextView courseRoom = (TextView) convertView.findViewById(R.id.course_room);
        TextView courseSchedule = (TextView) convertView.findViewById(R.id.course_schedule);

        courseName.setText(course.className);
        courseProf.setText("Professor: " + course.professor);
        courseRoom.setText("Location: " + course.classroom);
        courseSchedule.setText("Schedule: ");

        return convertView;
    }
}
