/*
 * @author: Jacob Merki
 * @description: This file defines the AddCourseFragment which presents a form to the user to
 * create a Course with a title, professor, professor email, classroom and schedule for the course.
 */
package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.sql.Time;
import java.util.Date;

/*
 * This class defines the AddCourseFragment that presents a form to the user to fill with
 * information about a new Course. Once the course is submitted the user will be returned to the
 * CoursesFragment.
 */
public class AddCourseFragment extends Fragment {

    /*
     * These attributes are used for creating the string of the course's schedule and keep track
     * of the activity containing the fragment
     */
    private final Integer[] dayCheckboxIds = {R.id.check_mon, R.id.check_tue, R.id.check_wed,
            R.id.check_thur, R.id.check_fri};
    private final String[] dayStrings = {"M", "Tu", "W", "Th", "F"};
    public Activity containerActivity;

    /*
     * This method is a constructor for the AddCourseFragment that sets the containing activity
     * of the fragment.
     *
     * This method takes nothing and returns an AddCourseFragment.
     */
    public AddCourseFragment(Activity container){
        containerActivity = container;
    }


    /*
     * This method creates the view for the AddCourseFragment by inflating the proper layout,
     * initializing the time pickers and setting the listener for the button that registers the
     * new course.
     *
     * This method takes a LayoutInflater, ViewGroup and Bundle and returns a View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_add_course, fragContainer, false);
        inflatedView.setBackgroundColor(getResources().getColor(R.color.tintWhite));
        inflatedView.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);

        //Add click listener for add class button
        Button register = inflatedView.findViewById(R.id.register_course);
        register.setOnClickListener(new RegisterCourse());

        //Initialize time pickers
        TimePicker startTime = (TimePicker) inflatedView.findViewById(R.id.picker_start);
        TimePicker endTime = (TimePicker) inflatedView.findViewById(R.id.picker_end);

        startTime.setHour(12); startTime.setMinute(00);
        endTime.setHour(12); endTime.setMinute(00);

        return inflatedView;
    }

    /*
     * This class defines the listener for the button that registers the new course, which takes
     * the information filled out in the form, constructs a new Cours eobject and adds the new
     * course to the ViewModel for the app.
     */
    public class RegisterCourse implements View.OnClickListener {

        /*
         * This method constructs a new Course from the form information and adds it the the list of
         * courses in the app's ViewModel.
         *
         * This method takes the View that called it and returns nothing.
         */
        @Override
        public void onClick(View v) {

            int courseDays = 0;
            String courseTimes = "";

            //Get course information
            EditText name = (EditText) getActivity().findViewById(R.id.edit_name);
            EditText prof = (EditText) getActivity().findViewById(R.id.edit_prof);
            EditText profEmail = (EditText) getActivity().findViewById(R.id.edit_email);
            EditText room = (EditText) getActivity().findViewById(R.id.edit_room);

            TimePicker courseStart = (TimePicker) getActivity().findViewById(R.id.picker_start);
            TimePicker courseEnd = (TimePicker) getActivity().findViewById(R.id.picker_end);

            courseTimes = createScheduleString(courseStart, courseEnd);
            courseDays = createDaysInt();

            //Construct new course and add to ViewModel, notify adpater of new data
            Course newCourse = new Course(name.getText().toString(), prof.getText().toString(),
                    profEmail.getText().toString(), room.getText().toString(), courseTimes,
                    courseDays);

            SchedulerViewModel model =
                    ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);

            model.addCourse(newCourse);
            ((MainActivity) getActivity()).coursesFrag.coursesAdapter.notifyDataSetChanged();

            //Return to main courses fragment
            ((MainActivity) getActivity()).returnToCoursesFragment();
        }
    }

    /*
     * This method creates an integer that represents which days of the week a course is on, with
     * Monday being represented as 1, Tuesday as 2 and so on. For example, if a course is all
     * five days of the work week it's integer is 12345. If just Monday and Wednesday the int is 13.
     *
     * This method takes nothing and returns an int.
     */
    public int createDaysInt(){
        int daysInt = 0;
        for(int i = 0; i < 5; i++){
            CheckBox dayBox = getActivity().findViewById(dayCheckboxIds[i]);
            if(dayBox != null &&dayBox.isChecked()) daysInt = (daysInt * 10) + (i + 1);
        }
        return daysInt;
    }

    /*
     * This method creates a string that represents the days and times that a course takes place
     * based on the days checked off and the times selected in the time pickers.
     *
     * This method takes the TimePicker for the course start, TimePicker for course end and returns
     * a String.
     */
    public String createScheduleString(TimePicker start, TimePicker end){
        String days = "";

        for(int i = 0; i < 5; i++){
            CheckBox dayBox = getActivity().findViewById(dayCheckboxIds[i]);
            if(dayBox != null && dayBox.isChecked()) days += dayStrings[i];
        }

        //Check if AM or PM
        String startAMPM = "AM", endAMPM = "AM";
        if(start.getHour() > 11) startAMPM = "PM";
        if(end.getHour() > 11) endAMPM = "PM";

        //Check if prefix 0 is needed for sub-10 minutes
        String startPrefix0 = "", endPrefix0 = "";
        if(start.getMinute() < 10) startPrefix0 = "0";
        if(end.getMinute() < 10) endPrefix0 = "0";

        //Get hours and minutes, hours in 24 time by default
        Integer startHour = start.getHour();
        if(startHour > 12) startHour -= 12;
        Integer startMinute = start.getMinute();

        Integer endHour = end.getHour();
        if(endHour > 12) endHour -= 12;
        Integer endMinute = end.getMinute();

        //Days + start time + end time
        return days + " " + startHour.toString() + ":" + startPrefix0 + startMinute.toString() +
                " " + startAMPM + " - " + endHour.toString() + ":" + endPrefix0 +
                endMinute.toString() + " " + endAMPM;
    }
}
