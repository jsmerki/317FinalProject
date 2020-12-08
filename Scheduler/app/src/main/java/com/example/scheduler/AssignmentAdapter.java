/*
 * @author: Jacob Merki
 * @description: This file defines the AssignmentAdapter class that extends the ArrayAdapter class
 * in order to display the list of assignments when a course is viewed in the EditCourseFragment.
 */
package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
 * This class defines the way in which assignments are viewed in the ListView by displaying their
 * name, due date and description, as well as a couple of buttons that are defined in the layout
 * file.
 */
public class AssignmentAdapter extends ArrayAdapter<Assignment> {

    /*
     * This constructor calls the super constructor of the ArrayAdapter class by passing it the
     * array of assignments and the context of the app.
     *
     * This method takes Context, ArrayList and returns an AssignmentAdapter.
     */
    public AssignmentAdapter(Context c, ArrayList<Assignment> assignments){
        super(c, 0, assignments);
    }

    /*
     * This method constructs the view for individual assignments in the list by setting the text
     * to be displayed for each of the views.
     *
     * This method takes int, View and ViewGroup and returns a View.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        //Get the assignment and inflate the view if necessary
        Assignment assign = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_assignment_row,
                    parent, false);
        }

        //Get all TextViews and set their text
        TextView assignName = (TextView) convertView.findViewById(R.id.assign_title);
        TextView assignDue = (TextView) convertView.findViewById(R.id.assign_due);
        TextView description = (TextView) convertView.findViewById(R.id.assign_description);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        assignName.setText(assign.assignName);
        assignDue.setText("Due By: " + formatter.format(assign.dueDate));
        description.setText(assign.description);

        return convertView;
    }
}
