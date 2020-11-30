package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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

        final String[] days = {"M", "Tu", "W", "Th", "F"};

        int currentDay = new Date().getDay();
        currentDay = 1;
        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_schedule, fragContainer, false);

        //Get courses and iterate through them to see if they are scheduled for today,
        SchedulerViewModel model =
                ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);
        ArrayList<Course> courses = model.getCourses();

        ArrayList<Course> coursesToday = new ArrayList<Course>();
        ArrayList<Assignment> assignsToday = new ArrayList<Assignment>();

        for(Course course: courses){
            //Monday-Friday is 1-5
            if(currentDay > 0 && currentDay < 6){
                if(course.scheduleStr.contains(days[currentDay + 1])) {
                    System.out.println(course.className);
                    coursesToday.add(course);
                }
            }
            else{
                //Display info for Monday with not at the top that this schedule is for monday!!!
                if(course.scheduleStr.contains(days[0])) {
                    System.out.println(course.className);
                    coursesToday.add(course);
                }
            }

            //Go through the assignments
            for(Assignment assign: course.allAssignments){
                if(assign.getDueDate().getDate() == currentDay){
                    assignsToday.add(assign);
                }
            }
        }

        //Set TextView headers and ListView content
        TextView classHeader = inflatedView.findViewById(R.id.schedule_class_header);
        TextView assignHeader = inflatedView.findViewById(R.id.schedule_assignment_header);
        ListView coursesTodayList = inflatedView.findViewById(R.id.schedule_class_list);
        ListView assignsTodayList = inflatedView.findViewById(R.id.schedule_assignment_list);

        //Set text for headers
        SimpleDateFormat fullFormatter = new SimpleDateFormat("EEE dd MMM");
        SimpleDateFormat partialFormatter = new SimpleDateFormat("dd MMM");
        classHeader.setText("Classes: " + fullFormatter.format(new Date()));
        assignHeader.setText("Assignments: " + fullFormatter.format(new Date()));
        //Account for if today is a weekend, display for monday
        if(currentDay == 0 || currentDay == 6) {
            classHeader.setText("Classes: Next Monday");
        }

        //Set ListView stuff
        ScheduleCourseAdapter courseAdapter = new ScheduleCourseAdapter(getContext(), coursesToday);
        coursesTodayList.setAdapter(courseAdapter);

        return inflatedView;
    }
}
