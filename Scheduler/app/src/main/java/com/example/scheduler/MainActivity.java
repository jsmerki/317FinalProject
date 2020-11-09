package com.example.scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoursesFragment coursesFrag = new CoursesFragment(this);
        FragmentTransaction coursesTransaction = getSupportFragmentManager().beginTransaction();
        coursesTransaction.add(R.id.fragment_container, coursesFrag);
        coursesTransaction.addToBackStack(null);
        coursesTransaction.commit();
    }

    public void insertAddCourseFragment(View view){
        AddCourseFragment addFrag = new AddCourseFragment(this);

        FragmentTransaction addCourseTransaction = getSupportFragmentManager().beginTransaction();
        addCourseTransaction.replace(R.id.fragment_container, addFrag);
        addCourseTransaction.addToBackStack(null);
        addCourseTransaction.commit();

    }
}