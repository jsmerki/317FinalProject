/*
 * @author: Jacob Merki
 * @description: This file defines the ScheduleAssignmentAdapter class that extends the ArrayAdapter
 * class in order to display the list of assignments that are due on a given day with a checkbox
 * so that the user can mark what assignments have been completed.
 */
package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
 * This class defines the way in which assignments are viewed in the ListView of the Schedule
 * fragment by displaying their name, the name of the course they belong to and a checkbox to
 * mark completed assignments for the day.
 */
public class ScheduleAssignmentAdapter extends ArrayAdapter<Assignment> {

    /*
     * This constructor calls the super constructor of the ArrayAdapter class by passing it the
     * array of assignments and the context of the app.
     *
     * This method takes Context, ArrayList and returns an AssignmentAdapter.
     */
    public ScheduleAssignmentAdapter(Context c, ArrayList<Assignment> assigns){
        super(c, 0, assigns);
    }

    /*
     * This method constructs the view for individual assignments in the list by setting the text
     * to be displayed for each of the views.
     *
     * This method takes int, View and ViewGroup and returns a View.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Get the assignment and inflate the view if necessary
        Assignment assign = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_sched_assign_row,
                    parent, false);
        }

        //Set the assignment and course name for the views
        TextView assignName = (TextView) convertView.findViewById(R.id.sched_assignrow_name);
        TextView assignCourse = (TextView) convertView.findViewById(R.id.sched_assignrow_course);

        assignName.setText(assign.assignName);
        assignCourse.setText(assign.getCourse().className);

        return convertView;
    }
}
