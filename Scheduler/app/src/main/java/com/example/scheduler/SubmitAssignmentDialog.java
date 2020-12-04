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

public class SubmitAssignmentDialog extends DialogFragment {

    View inflatedView;
    Course owningCourse;
    Spinner categories;

    String assignName;
    String assignDescr;

    public SubmitAssignmentDialog(Course course){
        this.owningCourse = course;
    }

    public void setAssignmentInfo(String name, String descr){
        this.assignName = name;
        this.assignDescr = descr;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder gradingBuilder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        inflatedView = inflater.inflate(R.layout.dialog_submit_assignment, null);

        //Set Spinner info
        categories = inflatedView.findViewById(R.id.grade_categories_spinner);
        ArrayAdapter<Grading> spinnerAdapter = new ArrayAdapter<Grading>(getActivity(),
                R.layout.spinner_grading_name, owningCourse.gradingCategories);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_grading_name);
        categories.setAdapter(spinnerAdapter);

        //Set the dialog view from the layout and implement button listeners
        gradingBuilder.setView(inflatedView)
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Grading category = (Grading) categories.getSelectedItem();
                        EditText pointsEarned = (EditText) inflatedView.
                                findViewById(R.id.edit_points_earned);
                        EditText maxPoints = (EditText) inflatedView.
                                findViewById(R.id.edit_points_max);

                        Assignment graded = owningCourse.findAssignment(assignName, assignDescr);
                        graded.setScores(Float.parseFloat(maxPoints.getText().toString()),
                                Float.parseFloat(pointsEarned.getText().toString()));
                        System.out.println(graded.assignName + " " + graded.pointsEarned + "/" + graded.pointsOutOf + " Submitted");
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
