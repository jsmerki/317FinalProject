/*
 * @author: Jacob Merki
 * @description: This file defines the HelpFragment which displays usable help information and
 * answers possible questions.
 */
package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/*
 * This class defines the HelpFragment which only needs to inflate and return the view since all of
 * the TextViews and string resources are defined in XML.
 */
public class HelpFragment extends Fragment {

    //Keep track of container activity
    public Activity containerActivity;

    /*
     * This constructor creates a new HelpFragment and sets the container activity.
     *
     * This method takes an Activity and returns a HelpFragment.
     */
    public HelpFragment(Activity container){
        this.containerActivity = container;
    }

    /*
     * This method creates the view for the HelpFragment by inflating the view from the XML layout.
     *
     * This method takes a LayoutInflater, ViewGroup and Bundle and returns a View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate and return view
        View inflatedView = inflater.inflate(R.layout.fragment_help, fragContainer, false);

        return inflatedView;
    }
}
