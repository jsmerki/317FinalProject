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
public class CalendarReminderDialog extends DialogFragment {

    /*
     *These attributes keep track of the view of the dialog, the assignment which is being given
     * a reminder and defines some final variables for determining the minutes of the reminder.
     */
    public View inflatedView;
    public Assignment assignToRemind;
    final String[] reminderTypes = {"Hours", "Days", "Weeks"};
    final int HOUR_MINS = 60;
    final int DAY_MINS = 1440;
    final int WEEK_MINS = 10080;

    /*
     * This constructor creates a new CalendarReminderDialog and sets the assignment that is getting
     * a reminder.
     *
     * This method takes an Assignment and returns a CalendarReminderDialog.
     */
    public CalendarReminderDialog(Assignment assign){
        this.assignToRemind = assign;
    }

    /*
     * This method creates the dialog based on the desired layout, sets the Spinner information
     * for the kinds of reminders available and sets the responding code that sets the code
     * that will get the reminder info if desired and calls the function to save the event.
     *
     * This method takes a Bundle and returns a Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder reminderBuilder = new AlertDialog.Builder(getActivity());
        //Get the layout inflater and inflate the view
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        inflatedView = inflater.inflate(R.layout.dialog_reminder, null);

        //Get the spinner and prepare and set the adapter
        final Spinner typesSpinner = (Spinner) inflatedView.findViewById(R.id.reminder_type_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_general_name, reminderTypes);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_general_name);
        typesSpinner.setAdapter(spinnerAdapter);

        //Set the view with positive and negative button actions
        reminderBuilder.setView(inflatedView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int multiplier = -1;

                        //Get the number entered by the user, parse it and determine the type
                        EditText reminderLength = (EditText) inflatedView
                                .findViewById(R.id.reminder_num);

                        int baseLength = Integer.parseInt(reminderLength.getText().toString());
                        String reminderType = (String) typesSpinner.getSelectedItem();

                        //Determine reminder
                        if(reminderType.equals("Hours")) multiplier = HOUR_MINS;
                        if(reminderType.equals("Days")) multiplier = DAY_MINS;
                        if(reminderType.equals("Weeks")) multiplier = WEEK_MINS;

                        //Save event with multiplied number of reminder minutes
                        ((MainActivity) getActivity()).saveInCalendar(assignToRemind,
                                baseLength * multiplier);
                    }
                })
                .setNegativeButton("No Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel and save the event without a reminder
                        CalendarReminderDialog.this.getDialog().cancel();
                        ((MainActivity) getActivity()).saveInCalendar(assignToRemind, -1);
                    }
                });

        return reminderBuilder.create();
    }

}
