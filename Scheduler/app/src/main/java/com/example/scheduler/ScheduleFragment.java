package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

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

        return inflatedView;
    }
}
