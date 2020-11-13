package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

//Add tasks/assignments or display course info
public class EditCourseFragment extends Fragment {

    public Activity containerActivity;
    public Course courseToEdit;

    public EditCourseFragment(Activity container, Course editCourse){
        this.containerActivity = container;
        this.courseToEdit = editCourse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_edit_course, fragContainer, false);

        TextView editClassTitle = (TextView) inflatedView.findViewById(R.id.edit_class_title);
        editClassTitle.setText(courseToEdit.className);

        return inflatedView;
    }
}
