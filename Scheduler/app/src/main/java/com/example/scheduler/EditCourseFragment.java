/*
 * @author: Jacob Merki
 * @description: This file defines the EditCourseFragment which displays basic course info and
 * allows users to add assignments to their course and interact with the different assignments.
 */
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

/*
 * This class defines the EditCourseFragment that lists to the user all of the assignments currently
 * in their course and allows them to add new ones to the course. If a new assignment is added the
 * an instance of the AddAssignmentFragment replaces this fragment.
 */
public class EditCourseFragment extends Fragment {

    //Keep track of the container activity, the course being edited and the list adapter
    public Activity containerActivity;
    public Course courseToEdit;
    public AssignmentAdapter assignAdapter;

    /*
     * This constructor creates a new EditCourseFragment and sets the container activity and
     * course to edit.
     *
     * This method takes Activity, Course and returns an EditCourseFragment.
     */
    public EditCourseFragment(Activity container, Course editCourse){
        this.containerActivity = container;
        this.courseToEdit = editCourse;
    }

    /*
     * This getter returns the course being edited for use in other parts of the app.
     */
    public Course getCourseToEdit(){
        return courseToEdit;
    }

    /*
     * This method creates the view for the EditCourseFragment by inflating the proper layout,
     * displaying the course name and professor email and setting the adapter for the assignment
     * list.
     *
     * This method takes a LayoutInflater, ViewGroup and Bundle and returns a View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_edit_course, fragContainer, false);

        //Set the information to be displayed in the textviews
        TextView editClassTitle = (TextView) inflatedView.findViewById(R.id.edit_class_title);
        TextView profEmail = (TextView) inflatedView.findViewById(R.id.professor_email_link);
        editClassTitle.setText(courseToEdit.className);
        profEmail.setText(courseToEdit.profEmail);

        //Set the adapter for the list
        assignAdapter = new AssignmentAdapter(getContext(), courseToEdit.getAssignments());
        ListView coursesList = (ListView) inflatedView.findViewById(R.id.list_assignments);
        coursesList.setAdapter(assignAdapter);

        return inflatedView;
    }

    /*
     * This method quickly notifies the assignment adapter to a change in data.
     */
    public void assignmentsChanged(){
        assignAdapter.notifyDataSetChanged();
    }
}

