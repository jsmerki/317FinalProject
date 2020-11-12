package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

//Form to submit initial class information
public class AddCourseFragment extends Fragment {

    Activity containerAcitivty;

    public AddCourseFragment(Activity container){
        containerAcitivty = container;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_add_course, fragContainer, false);

        //Add click listener for add class button
        Button register = inflatedView.findViewById(R.id.register_course);
        register.setOnClickListener(new RegisterCourse());

        return inflatedView;
    }

    public class RegisterCourse implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            //Get course information
            EditText name = (EditText) getActivity().findViewById(R.id.edit_name);
            EditText prof = (EditText) getActivity().findViewById(R.id.edit_prof);
            EditText room = (EditText) getActivity().findViewById(R.id.edit_room);

            Course newCourse = new Course(name.getText().toString(), prof.getText().toString(),
                    room.getText().toString(), 0);

            SchedulerViewModel model =
                    ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);

            model.addCourse(newCourse);
            ((MainActivity) getActivity()).coursesFrag.coursesAdapter.notifyDataSetChanged();

            System.out.println("New Course Added: ");
            System.out.println(newCourse.className + " " + newCourse.professor + " "
                    + newCourse.classroom);

            ((MainActivity) getActivity()).returnToCoursesFragment();
        }
    }

}
