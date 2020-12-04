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

public class AddGradingDialog extends DialogFragment {

    View inflatedView;

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

    public class UpdatePercentText implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView percentText = inflatedView.findViewById(R.id.grading_percent_text);
            percentText.setText(progress + "%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


}
