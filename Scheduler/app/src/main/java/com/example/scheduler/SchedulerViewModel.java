package com.example.scheduler;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

//Holds classes, assignments,...
public class SchedulerViewModel extends ViewModel {

    private ArrayList<Course> courses = new ArrayList<Course>();
    private ArrayList<Assignment> assignments = new ArrayList<Assignment>();

    public void setCourses(ArrayList<Course> courseList){
        this.courses = courseList;
    }

    public void setAssignments(ArrayList<Assignment> assignmentsList){
        this.assignments = assignmentsList;
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }

    public ArrayList<Assignment> getAssignments(){
        return assignments;
    }

    public void addCourse(Course newCourse){
        this.courses.add(newCourse);
    }

    public void addAssignment(Assignment newAssign){
        this.assignments.add(newAssign);
    }
}
