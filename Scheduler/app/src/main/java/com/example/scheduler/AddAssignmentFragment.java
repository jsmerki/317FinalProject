package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Date;

public class AddAssignmentFragment extends Fragment {

    Activity containerActivity;
    Course owningCourse;

    public AddAssignmentFragment(Activity container, Course owner){
        this.containerActivity = container;
        this.owningCourse = owner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_add_assignment, fragContainer, false);
        inflatedView.setBackgroundColor(getResources().getColor(R.color.tintWhite));
        inflatedView.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);

        //Get desired course

        //Set click listener for button
        Button addAssignment = inflatedView.findViewById(R.id.confirm_assignment);
        addAssignment.setOnClickListener(new AddAssignment());

        return inflatedView;
    }

    public class AddAssignment implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            System.out.println("Adding Assignment");
            EditText editName = (EditText) getActivity().findViewById(R.id.edit_assign_name);
            String assignName = editName.getText().toString();

            RadioGroup type = (RadioGroup) getActivity().findViewById(R.id.assignment_type);
            int chosenType = type.getCheckedRadioButtonId();

            DatePicker dueDatePicker = (DatePicker) getActivity().findViewById(R.id.due_date);
            Date dueDate = new Date(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth());
            System.out.println(dueDate.getYear());

            EditText editDescr = (EditText) getActivity().findViewById(R.id.edit_description);
            String description = editDescr.getText().toString();

            Assignment newAssignment = new Assignment(owningCourse, assignName, chosenType, dueDate,
                    description);

            owningCourse.addAssignment(newAssignment);
            System.out.println("Added Assignment");

            ((MainActivity) getActivity()).returnToEditCourseFragment();
        }
    }
}
