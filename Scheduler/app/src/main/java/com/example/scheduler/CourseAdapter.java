/*
 * @author: Jacob Merki
 * @description: This file defines the CourseAdapter class that extends the ArrayAdapter class
 * in order to display the list of courses that the user has registered in.
 */
package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * This class defines the way in which courses are viewed in the ListView by displaying their
 * name, professor, classroom and the days and time that the course occurs.
 */
public class CourseAdapter extends ArrayAdapter<Course> {

    /*
     * This constructor calls the super constructor of the ArrayAdapter class by passing it the
     * array of courses and the context of the app.
     *
     * This method takes Context, ArrayList and returns a CourseAdapter.
     */
    public CourseAdapter(Context c, ArrayList<Course> courses){
        super(c, 0, courses);
    }

    /*
     * This method constructs the view for individual courses in the list by setting the text
     * to be displayed for each of the views.
     *
     * This method takes int, View and ViewGroup and returns a View.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        //Get course and inflate view if necessary
        Course course = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_course_row,
                    parent, false);
        }

        //Get all four text views and set their info based on the strings in the Course object
        TextView courseName = (TextView) convertView.findViewById(R.id.course_title);
        TextView courseProf = (TextView) convertView.findViewById(R.id.course_prof);
        TextView courseRoom = (TextView) convertView.findViewById(R.id.course_room);
        TextView courseSchedule = (TextView) convertView.findViewById(R.id.course_schedule);

        courseName.setText(course.className);
        courseProf.setText("Professor: " + course.professor);
        courseRoom.setText("Location: " + course.classroom);
        courseSchedule.setText("Schedule: " + course.scheduleStr);

        return convertView;
    }
}
