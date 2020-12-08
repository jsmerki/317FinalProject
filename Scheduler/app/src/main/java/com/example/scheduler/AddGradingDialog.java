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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/*
 * This class defines the AddGradingDialog class that shows a Dialog with an EditText and SeekBar
 * to set the name and percent weight of the new grading category of a course.
 */
public class AddGradingDialog extends DialogFragment {


    //Reference to the inflated view of the dialog
    public View inflatedView;

    /*
     * This method creates the dialog based on the desired layout, sets the listener for the
     * SeekBar and set's the responding code depending on which button of the dialog is chosen.
     *
     * This method takes a Bundle and returns a Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder gradingBuilder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        inflatedView = inflater.inflate(R.layout.dialog_add_grading, null);

        //Set seekbar listener
        final SeekBar percentBar = inflatedView.findViewById(R.id.grading_percent);
        percentBar.setOnSeekBarChangeListener(new UpdatePercentText());

        //Set the dialog view from the layout and implement button listeners
        gradingBuilder.setView(inflatedView)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Get entered information and create new Grading
                        EditText gradingName = inflatedView.findViewById(R.id.grading_name);
                        String gradingNameStr = gradingName.getText().toString();
                        int percentage = percentBar.getProgress();
                        Grading newGrade = new Grading(gradingNameStr, percentage);
                        ((MainActivity) getActivity()).addGradingToCourse(newGrade);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddGradingDialog.this.getDialog().cancel();
                    }
                });

        return gradingBuilder.create();
    }

    /*
     * This class defines a simple SeekBar listener that updates a TextView to show the progress
     * of the SeekBar to help the user visualize the percent they are choosing.
     */
    public class UpdatePercentText implements SeekBar.OnSeekBarChangeListener{

        /*
         * This method updates the TextView with the current progress of the SeekBar.
         *
         * This method takes a SeekBar, an int and a boolean and returns nothing.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView percentText = inflatedView.findViewById(R.id.grading_percent_text);
            percentText.setText(progress + "%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //NOT NEEDED
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //NOT NEEDED
        }
    }


}
