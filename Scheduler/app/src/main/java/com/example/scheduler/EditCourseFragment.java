package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//Add tasks/assignments or display course info
public class EditCourseFragment extends Fragment {

    public Activity containerActivity;
    public Course courseToEdit;
    public AssignmentAdapter assignAdapter;

    public EditCourseFragment(Activity container, Course editCourse){
        this.containerActivity = container;
        this.courseToEdit = editCourse;
    }

    public Course getCourseToEdit(){
        return courseToEdit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_edit_course, fragContainer, false);

        TextView editClassTitle = (TextView) inflatedView.findViewById(R.id.edit_class_title);
        TextView profEmail = (TextView) inflatedView.findViewById(R.id.professor_email_link);
        editClassTitle.setText(courseToEdit.className);
        profEmail.setText(courseToEdit.profEmail);

        assignAdapter = new AssignmentAdapter(getContext(), courseToEdit.getAssignments());
        ListView coursesList = (ListView) inflatedView.findViewById(R.id.list_assignments);
        coursesList.setAdapter(assignAdapter);

        return inflatedView;
    }

    public void assignmentsChanged(){
        assignAdapter.notifyDataSetChanged();
    }
}

