/*
 * @author: Jacob Merki
 * @description: This file defines the ScheduleFragment which displays the courses that occur and
 * assignments that are due for a given day, and allows users to request weather information.
 */
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

/*
 * This class defines the ScheduleFragment that presents relevant course and assignment information
 * for a given day by determining which courses occur and which assignments are due, constructing
 * lists of that information and sets adapters for those ListViews.
 */
public class ScheduleFragment extends Fragment {

    //Keep track of the container activity
    Activity containerActivity;

    /*
     * This constructor creates a new ScheduleFragment and sets the container activity.
     *
     * This method takes an Activity and returns a ScheduleFragment.
     */
    public ScheduleFragment(Activity container){
        this.containerActivity = container;
    }

    /*
     * This method creates the view for the ScheduleFragment by inflating the proper layout,
     * determining the courses and assignments that belong to the day, constructing lists and
     * setting them with correct adapters.
     *
     * This method takes a LayoutInflater, ViewGroup and Bundle and returns a View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        final String[] days = {"M", "Tu", "W", "Th", "F"};

        int currentDay = new Date().getDay();
        int currentDate = new Date().getDate();

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_schedule, fragContainer, false);

        //Get courses and iterate through them to see if they are scheduled for today,
        SchedulerViewModel model =
                ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);
        ArrayList<Course> courses = model.getCourses();

        ArrayList<Course> coursesToday = new ArrayList<Course>();
        ArrayList<Assignment> assignsToday = new ArrayList<Assignment>();

        for(Course course: courses){
            //Get only the days that the course occurs
            String[] schedInfo = course.scheduleStr.split(" ");
            String courseDays = schedInfo[0];
            //Monday-Friday is 1-5
            if(currentDay > 0 && currentDay < 6){
                if(courseDays.contains(days[currentDay - 1])) {
                    coursesToday.add(course);
                }
            }
            else{
                //Display info for Monday with not at the top that this schedule is for monday!!!
                if(course.scheduleStr.contains(days[0])) {
                    coursesToday.add(course);
                }
            }

            //Go through the assignments of the course
            for(Assignment assign: course.allAssignments){
                if(assign.getDueDate().getDate() == currentDate){
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

        //Set ListView adapters
        ScheduleCourseAdapter courseAdapter = new ScheduleCourseAdapter(getContext(), coursesToday);
        coursesTodayList.setAdapter(courseAdapter);

        ScheduleAssignmentAdapter assignAdapter = new ScheduleAssignmentAdapter(getContext(),
                assignsToday);
        assignsTodayList.setAdapter(assignAdapter);

        return inflatedView;
    }
}
