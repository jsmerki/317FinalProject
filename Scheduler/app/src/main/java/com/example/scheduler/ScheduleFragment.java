package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Date;

public class ScheduleFragment extends Fragment {

    Activity containerActivity;
    SchedulerViewModel model;

    public ScheduleFragment(Activity container){
        this.containerActivity = container;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        int currentDay = new Date().getDay();
        System.out.println(currentDay);
        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_schedule, fragContainer, false);

        //Get courses and iterate through them to see if they are scheduled for today,
        SchedulerViewModel model =
                ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);
        ArrayList<Course> courses = model.getCourses();

        System.out.println(model.getCourses().size());
        for(Course course: courses){
            System.out.println(course.className);
            String[] schedInfo = course.scheduleStr.split(" ");
            for(String str: schedInfo){
                System.out.println(str);
            }
        }
        System.out.println("DONE");

        return inflatedView;
    }
}
