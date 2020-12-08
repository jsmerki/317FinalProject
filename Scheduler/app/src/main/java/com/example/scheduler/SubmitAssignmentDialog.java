/*
 * @author: Jacob Merki
 * @description: This file defines the AddGradingDialog which creates a new Grading category for a
 * course by showing a Dialog where the user provides a name and percent weight for the category.
 */
package com.example.scheduler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/*
 * This class defines the AddGradingDialog class that shows a Dialog with an EditText and SeekBar
 * to set the name and percent weight of the new grading category of a course.
 */
public class SubmitAssignmentDialog extends DialogFragment {

    /*
     * These attributes keep track of the inflated view, the course that owns the assignment, a
     * spinner for grading categories and assignment info
     */
    public View inflatedView;
    public Course owningCourse;
    public Spinner categories;
    public String assignName;
    public String assignDescr;

    /*
     * This constructor creates a new SubmitAssignmentDialog and sets the owning course.
     *
     * This method takes a Course and returns a SubmitAssignmentDialog.
     */
    public SubmitAssignmentDialog(Course course){
        this.owningCourse = course;
    }

    /*
     * This method sets the assignment info so that it can be removed from the Course and added to
     * the Grading category.
     *
     * This method takes a String name and String description and returns nothing.
     */
    public void setAssignmentInfo(String name, String descr){
        this.assignName = name;
        this.assignDescr = descr;
    }

    /*
     * This method creates the dialog based on the desired layout, sets the spinner info for the
     * different grading categories in the course and handles removing the assignment from the
     * course that owned it and adding it to the selected grading category.
     *
     * This method takes a Bundle and returns a Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder gradingBuilder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        inflatedView = inflater.inflate(R.layout.dialog_submit_assignment, null);

        //Set Spinner info
        categories = inflatedView.findViewById(R.id.grade_categories_spinner);
        ArrayAdapter<Grading> spinnerAdapter = new ArrayAdapter<Grading>(getActivity(),
                R.layout.spinner_general_name, owningCourse.gradingCategories);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_general_name);
        categories.setAdapter(spinnerAdapter);

        //Set the dialog view from the layout and implement button listeners
        gradingBuilder.setView(inflatedView)
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Get the selected grading category and assignment points
                        Grading category = (Grading) categories.getSelectedItem();
                        EditText pointsEarned = (EditText) inflatedView.
                                findViewById(R.id.edit_points_earned);
                        EditText maxPoints = (EditText) inflatedView.
                                findViewById(R.id.edit_points_max);

                        //Find the assignment, set its scored points, remove from the course
                        //and add to the grading
                        Assignment graded = owningCourse.findAssignment(assignName, assignDescr);
                        graded.setScores(Float.parseFloat(maxPoints.getText().toString()),
                                Float.parseFloat(pointsEarned.getText().toString()));
                        owningCourse.removeAssignment(assignName, assignDescr);
                        owningCourse.addGradedAssignment(category.categoryName, graded);

                        ((MainActivity) getActivity()).updateAssignmentsAdapter();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SubmitAssignmentDialog.this.getDialog().cancel();
                    }
                });


        return gradingBuilder.create();
    }
}
