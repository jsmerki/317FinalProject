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

public class CoursesFragment extends Fragment {

    Activity containerActivity;
    CourseAdapter coursesAdapter;

    public CoursesFragment(Activity container){
        containerActivity = container;
    }

    public CourseAdapter getCoursesAdapter(){
        return coursesAdapter;
    }

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

    public class EditClickedCourse implements AdapterView.OnItemClickListener{
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
