/*
 * @author: Jacob Merki
 * @description: This file defines the Assignment class which keeps track of an Assignment's name,
 * the course it belongs to, type, dueDate, description, points earned and points possible.
 */
package com.example.scheduler;

import java.io.Serializable;
import java.util.Date;

enum type {ASSIGN_READING, ASSIGN_HOMEWORK, ASSIGN_TEST}

/*
 * This class defines the structure of Assignments as used in the app as well as necessary setters
 * and getters.
 */
public class Assignment implements Serializable {

    /*
     * These attributes keep track of the necessary information for an assignment
     */
    private Course course;
    public String assignName;
    private type assignType;
    public Date dueDate;
    public String description;
    public float pointsOutOf;
    public float pointsEarned;

    /*
     * This constructor creates a new assignment by setting the provided values and initializing
     * the scores to 0.
     *
     * This method takes a Course, String, int, Date and String and returns nothing.
     */
    public Assignment(Course c, String name, int assign, Date date, String descr){
        this.course = c;
        this.dueDate = date;
        this.assignName = name;
        this.description = descr;
        this.pointsOutOf = 0.0f;
        this.pointsEarned = 0.0f;

        if(assign == R.id.assign_read) assignType = type.ASSIGN_READING;
        if(assign == R.id.assign_homework) assignType = type.ASSIGN_HOMEWORK;
        if(assign == R.id.assign_test) assignType = type.ASSIGN_TEST;
    }

    /*
     * These getters return the due date for the assignment and the course that the assignment
     * belongs to.
     *
     * The first getter returns a Date.
     * The second getter returns a Course.
     */
    public Date getDueDate(){
        return dueDate;
    }
    public Course getCourse(){ return course;}

    /*
     * This setter sets the earned and possible points for an assignment so that its grade can be
     * calculated when necessary.
     *
     * This method takes float, float and returns nothing.
     */
    public void setScores(float outOf, float earned){
        this.pointsOutOf = outOf;
        this.pointsEarned = earned;
    }
}
