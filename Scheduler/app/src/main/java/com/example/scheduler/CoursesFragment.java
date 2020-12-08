/*
 * @author: Jacob Merki
 * @description: This file defines the CoursesFragment which displays all of the user's registered
 * courses in a list view along with a button to add new courses.
 */
package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/*
 * This class defines the CoursesFragment functionality that displays all of the user's registered
 * courses in the ViewModel.
 */
public class CoursesFragment extends Fragment {

    //Keep track of the container activity and array adapter for courses
    public Activity containerActivity;
    public CourseAdapter coursesAdapter;

    /*
     * This constructor creates a new CoursesFragment and sets the container activity.
     *
     * This method takes an Activity and returns a CoursesFragment.
     */
    public CoursesFragment(Activity container){
        containerActivity = container;
    }

    /*
     * This method creates the view for the CoursesFragment by getting the array of courses
     * from the ViewModel and sets the adapter for the list of courses.
     *
     * This method takes a LayoutInflater, ViewGroup and Bundle and returns a View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_courses, fragContainer, false);

        //ListView and Adapter
        SchedulerViewModel model =
                ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);
        coursesAdapter = new CourseAdapter(getContext(), model.getCourses());
        ListView coursesList = (ListView) inflatedView.findViewById(R.id.courses_list);
        coursesList.setOnItemClickListener(new EditClickedCourse());
        coursesList.setAdapter(coursesAdapter);

        return inflatedView;
    }

    /*
     * This class defines the click listener that changes to the EditCourseFragment based on the
     * course clicked in the list.
     */
    public class EditClickedCourse implements AdapterView.OnItemClickListener{

        /*
         * This click listener gets the selected course from the ViewModel and calls the function
         * in MainActivity to switch to the EditCourse fragment.
         *
         * This method takes AdapterView, View, int and long and returns nothing.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("Changing to edit");
            SchedulerViewModel model =
                    ViewModelProviders.of(getActivity()).get(SchedulerViewModel.class);
            Course courseToEdit = model.getCourses().get(position);

            ((MainActivity) getActivity()).insertEditCoursesFragment(courseToEdit);
        }
    }

}
