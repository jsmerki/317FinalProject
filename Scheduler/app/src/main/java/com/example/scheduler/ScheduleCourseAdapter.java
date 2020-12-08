/*
 * @author: Jacob Merki
 * @description: This file defines the ScheduleCourseAdapter class that extends the ArrayAdapter
 * class in order to display the list of courses that occur on a given day.
 */
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

/*
 * This class defines the way in which courses are viewed in the ListView of the Schedule
 * fragment by displaying their name, the time that they occur and what room they occur in.
 */
public class ScheduleCourseAdapter extends ArrayAdapter<Course> {

    /*
     * This constructor calls the super constructor of the ArrayAdapter class by passing it the
     * array of courses and the context of the app.
     *
     * This method takes Context, ArrayList and returns an AssignmentAdapter.
     */
    public ScheduleCourseAdapter(Context c, ArrayList<Course> courses){
        super(c, 0, courses);
    }

    /*
     * This method constructs the view for individual courses in the list by setting the text
     * to be displayed for each of the views.
     *
     * This method takes int, View and ViewGroup and returns a View.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Get the course and inflate the view if necessary
        Course course = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_sched_course_row,
                    parent, false);
        }

        //Get views and begin setting info
        TextView courseName = (TextView) convertView.findViewById(R.id.sched_courserow_name);
        TextView courseTime = (TextView) convertView.findViewById(R.id.sched_courserow_time);
        TextView courseRoom = (TextView) convertView.findViewById(R.id.sched_courserow_room);

        courseName.setText(course.className);

        //Remove the string of days the course occurs on, not relevant
        List<String> schedSplit = Arrays.asList(course.scheduleStr.split(" "));
        String times = "";
        for(int i = 1; i < schedSplit.size(); i++){
            times = times + schedSplit.get(i) + " ";
        }
        courseTime.setText("Time: " + times);

        //Only set the room if a room exists, might get neglected more often by user
        if(course.classroom.length() > 0){
            courseRoom.setText("Room: " + course.classroom);
            courseRoom.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
