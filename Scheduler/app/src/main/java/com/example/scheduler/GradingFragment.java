package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

public class GradingFragment extends Fragment {

    public Activity containerActivity;
    public ArrayList<Course> coursesList = new ArrayList<Course>();

    public GradingFragment(Activity container, ArrayList<Course> courses){
        this.containerActivity = container;
        this.coursesList = courses;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_grading, fragContainer, false);

        //Get ArrayList of Courses to use in Spinner
        System.out.println(coursesList.get(0).className);

        //Set course name list for spinner
        Spinner courseSpinner = inflatedView.findViewById(R.id.course_spinner);
        ArrayAdapter<Course> spinnerAdapter = new ArrayAdapter<Course>(getActivity(),
                R.layout.spinner_course_name, coursesList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_course_name);

        courseSpinner.setAdapter(spinnerAdapter);

        return inflatedView;
    }

    public void getCourseNames(){
        SchedulerViewModel model =
                ViewModelProviders.of(this).get(SchedulerViewModel.class);
    }
}
