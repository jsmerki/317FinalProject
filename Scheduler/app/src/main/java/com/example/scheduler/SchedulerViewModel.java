package com.example.scheduler;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

//Holds classes, assignments,...
public class SchedulerViewModel extends ViewModel {

    private ArrayList<Course> courses;
    private ArrayList<Assignment> assignments;

    public ArrayList<Course> getCourses(){
        if (this.courses == null){
            return new ArrayList<Course>();
        }
        return courses;
    }

    public ArrayList<Assignment> getAssignments(){
        if (this.assignments == null){
            return new ArrayList<Assignment>();
        }
        return assignments;
    }

    public void addCourse(Course newCourse){
        this.courses.add(newCourse);
    }

    public void addAssignment(Assignment newAssign){
        this.assignments.add(newAssign);
    }
}
