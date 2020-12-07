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

public class CalendarReminderDialog extends DialogFragment {

    public View inflatedView;
    public Assignment assignToRemind;

    final String[] reminderTypes = {"Hours", "Days", "Weeks"};
    final int HOUR_MINS = 60;
    final int DAY_MINS = 1440;
    final int WEEK_MINS = 10080;

    public CalendarReminderDialog(Assignment assign){
        this.assignToRemind = assign;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder reminderBuilder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        inflatedView = inflater.inflate(R.layout.dialog_reminder, null);

        final Spinner typesSpinner = (Spinner) inflatedView.findViewById(R.id.reminder_type_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_general_name, reminderTypes);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_general_name);
        typesSpinner.setAdapter(spinnerAdapter);

        reminderBuilder.setView(inflatedView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int multiplier = 60; //Start off with hours multiplier
                        EditText reminderLength = (EditText) inflatedView
                                .findViewById(R.id.reminder_num);

                        int baseLength = Integer.parseInt(reminderLength.getText().toString());
                        String reminderType = (String) typesSpinner.getSelectedItem();

                        if(reminderType.equals("Hours")) multiplier = HOUR_MINS;
                        if(reminderType.equals("Days")) multiplier = DAY_MINS;
                        if(reminderType.equals("Weeks")) multiplier = WEEK_MINS;

                        ((MainActivity) getActivity()).saveInCalendar(assignToRemind,
                                baseLength * multiplier);
                    }
                })
                .setNegativeButton("No Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CalendarReminderDialog.this.getDialog().cancel();
                        ((MainActivity) getActivity()).saveInCalendar(assignToRemind, -1);
                    }
                });

        return reminderBuilder.create();
    }

}
