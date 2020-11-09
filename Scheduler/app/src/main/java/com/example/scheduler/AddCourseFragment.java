package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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

        return inflatedView;
    }

}
