/*
 * @author: Jacob Merki
 * @description: This file defines the AddAssignmentFragment which presents a form to the user to
 * create an assignment with a title, due date, assignment type and description.
 */
package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Date;

/*
 * This class defines the AddAssignmentFragment that displays a form to create a new assignment for
 * a specific course. Once the assignment is created it is added to the course and the user is
 * returned to the EditCourseFragment.
 */
public class AddAssignmentFragment extends Fragment {

    /*
     * These attributes are used for keeping track of the activity containing the fragment and the
     * course that the assignment will be assigned to.
     */
    public Activity containerActivity;
    public Course owningCourse;

    /*
     * This method is a constructor for the AddAssignmentFragment that sets the containing activity
     * and owning course of the assignment.
     *
     * This method takes nothing and returns an AddAssignmentFragment.
     */
    public AddAssignmentFragment(Activity container, Course owner){
        this.containerActivity = container;
        this.owningCourse = owner;
    }

    /*
     * This method creates the view for the AddAssignmentFragment by inflating the proper layout
     * and setting the listener for the button that creates the new assignment.
     *
     * This method takes a LayoutInflater, ViewGroup and Bundle and returns a View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_add_assignment, fragContainer, false);
        inflatedView.setBackgroundColor(getResources().getColor(R.color.tintWhite));
        inflatedView.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);

        //Set click listener for button
        Button addAssignment = inflatedView.findViewById(R.id.confirm_assignment);
        addAssignment.setOnClickListener(new AddAssignment());

        return inflatedView;
    }

    /*
     * This class defines the listener for the button that creates a new assignment by retrieving
     * the assignment info from the form and adding the assignment to the course.
     */
    public class AddAssignment implements View.OnClickListener {

        /*
         * This method pulls all of the data filled out on the form and uses it to construct a new
         * assignment for the desired course.
         *
         * This method takes the View that called it and returns nothing.
         */
        @Override
        public void onClick(View v) {

            //Get the assignment name, type, due date and description and create a new object
            EditText editName = (EditText) getActivity().findViewById(R.id.edit_assign_name);
            String assignName = editName.getText().toString();

            RadioGroup type = (RadioGroup) getActivity().findViewById(R.id.assignment_type);
            int chosenType = type.getCheckedRadioButtonId();

            DatePicker dueDatePicker = (DatePicker) getActivity().findViewById(R.id.due_date);
            Date dueDate = new Date(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth());

            EditText editDescr = (EditText) getActivity().findViewById(R.id.edit_description);
            String description = editDescr.getText().toString();

            Assignment newAssignment = new Assignment(owningCourse, assignName, chosenType, dueDate,
                    description);

            //Add to the course and return to the EditCourseFragment
            owningCourse.addAssignment(newAssignment);

            ((MainActivity) getActivity()).returnToEditCourseFragment();
        }
    }
}
