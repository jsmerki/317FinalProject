package com.example.scheduler;

import java.io.Serializable;
import java.util.Date;

enum type {ASSIGN_READING, ASSIGN_HOMEWORK, ASSIGN_TEST}

public class Assignment implements Serializable {

    private Course course;
    public String assignName;
    private type assignType;
    public Date dueDate;
    public String description;
    public float pointsOutOf;
    public float pointsEarned;

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

    public Date getDueDate(){
        return dueDate;
    }
    public Course getCourse(){ return course;}

    public void setScores(float outOf, float earned){
        this.pointsOutOf = outOf;
        this.pointsEarned = earned;
    }
}
