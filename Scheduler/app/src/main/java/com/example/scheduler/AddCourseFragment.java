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

//Form to submit initial class information
public class AddCourseFragment extends Fragment {

    private final Integer[] dayCheckboxIds = {R.id.check_mon, R.id.check_tue, R.id.check_wed,
            R.id.check_thur, R.id.check_fri};
    private final String[] dayStrings = {"M", "Tu", "W", "Th", "F"};

    public Activity containerAcitivty;

    public AddCourseFragment(Activity container){
        containerAcitivty = container;
    }

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

    public class RegisterCourse implements View.OnClickListener {

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

            Course newCourse = new Course(name.getText().toString(), prof.getText().toString(),
                    profEmail.getText().toString(), room.getText().toString(), courseTimes,
                    courseDays);

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

    public int createDaysInt(){
        int daysInt = 0;
        for(int i = 0; i < 5; i++){
            CheckBox dayBox = getActivity().findViewById(dayCheckboxIds[i]);
            if(dayBox != null &&dayBox.isChecked()) daysInt = (daysInt * 10) + (i + 1);
        }
        System.out.println(daysInt);
        return daysInt;
    }

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
