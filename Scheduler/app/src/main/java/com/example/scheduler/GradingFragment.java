package com.example.scheduler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public class GradingFragment extends Fragment {

    public Activity containerActivity;
    public ArrayList<Course> coursesList = new ArrayList<Course>();
    public GradingAdapter gradingAdapter;
    public ListView grades;

    public GradingFragment(Activity container, ArrayList<Course> courses){
        this.containerActivity = container;
        this.coursesList = courses;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragContainer,
                             Bundle savedInstanceState) {

        System.out.println("In Gradings");
        //Inflate layout to fit fragContainer View
        View inflatedView = inflater.inflate(R.layout.fragment_grading, fragContainer, false);

        //Set course name list for spinner
        Spinner courseSpinner = inflatedView.findViewById(R.id.course_spinner);
        courseSpinner.setOnItemSelectedListener(new ViewCourseGrading());
        ArrayAdapter<Course> spinnerAdapter = new ArrayAdapter<Course>(getActivity(),
                R.layout.spinner_course_name, coursesList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_course_name);

        courseSpinner.setAdapter(spinnerAdapter);
        grades = (ListView) inflatedView.findViewById(R.id.grades_list);

        //Try ListView again
        if(coursesList.size() > 0) {
            Course course = coursesList.get(courseSpinner.getSelectedItemPosition());
            gradingAdapter = new GradingAdapter(getContext(), course.getGradingCategories());
            grades.setAdapter(gradingAdapter);
        }
        else{
            gradingAdapter = new GradingAdapter(getContext(), new ArrayList<Grading>());
            grades.setAdapter(gradingAdapter);
        }

        return inflatedView;
    }

    public void addGradingInFragment(Grading grade){
        Spinner courseSpinner = getActivity().findViewById(R.id.course_spinner);
        int courseIndex = courseSpinner.getSelectedItemPosition();
        Course course = coursesList.get(courseIndex);
        course.addGrading(grade);
        gradingAdapter.updateGradeCategories(course.gradingCategories);
        gradingAdapter.notifyDataSetChanged();
        Toast gradeToast = Toast.makeText(getContext(),
                grade.categoryName + " added to " + course.className, Toast.LENGTH_SHORT);
        gradeToast.show();
        grades.setVisibility(View.VISIBLE);
    }

    public class ViewCourseGrading implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int courseIndex = parent.getSelectedItemPosition();
            Course course = coursesList.get(courseIndex);
            if(course.gradingCategories.size() == 0){
                grades.setVisibility(View.GONE);
            }
            gradingAdapter.updateGradeCategories(course.gradingCategories);
            gradingAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //NOTHING NEEDED
        }


    }
}
