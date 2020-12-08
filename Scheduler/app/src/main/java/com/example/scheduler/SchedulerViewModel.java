/*
 * @author: Jacob Merki
 * @description: This file defines the SchedulerViewModel which stores Course information of the
 * user to be used in various parts of the app.
 */
package com.example.scheduler;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

/*
 * This class defines the SchedulerViewModel that can store and update Courses read in from storage,
 * add new courses as the user registers them and returns them to other app components.
 */
public class SchedulerViewModel extends ViewModel {

    //Main list of courses registered by the user
    private ArrayList<Course> courses = new ArrayList<Course>();

    /*
     * These setters and getters are for the ArrayList courses as it is modified or needed
     * by other app components.
     */
    public void setCourses(ArrayList<Course> courseList){
        this.courses = courseList;
    }
    public ArrayList<Course> getCourses(){
        return courses;
    }

    /*
     * This method adds a new course to the model's list.
     *
     * This method takes a Course and returns nothing.
     */
    public void addCourse(Course newCourse){
        this.courses.add(newCourse);
    }
}
