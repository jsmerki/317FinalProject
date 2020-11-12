package com.example.scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public CoursesFragment coursesFrag = new CoursesFragment(this);
    public AddCourseFragment addFrag = new AddCourseFragment(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction coursesTransaction = getSupportFragmentManager().beginTransaction();
        coursesTransaction.add(R.id.fragment_container, coursesFrag);
        coursesTransaction.addToBackStack(null);
        coursesTransaction.commit();
    }

    public void insertAddCourseFragment(View view){

        FragmentTransaction addCourseTransaction = getSupportFragmentManager().beginTransaction();
        addCourseTransaction.replace(R.id.fragment_container, addFrag);
        addCourseTransaction.addToBackStack(null);
        addCourseTransaction.commit();
    }

    public void returnToCoursesFragment(){
        FragmentTransaction removeAddCourseTransaction =
                getSupportFragmentManager().beginTransaction();
        removeAddCourseTransaction.replace(R.id.fragment_container, coursesFrag);
        removeAddCourseTransaction.addToBackStack(null);
        removeAddCourseTransaction.commit();
    }

    public CoursesFragment getCoursesFragment(){
        return coursesFrag;
    }

}