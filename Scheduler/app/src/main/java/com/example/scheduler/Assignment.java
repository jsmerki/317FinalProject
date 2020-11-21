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

    public Assignment(Course c, String name, int assign, Date date, String descr){
        this.course = c;
        this.dueDate = date;
        this.assignName = name;
        this.description = descr;

        if(assign == R.id.assign_read) assignType = type.ASSIGN_READING;
        if(assign == R.id.assign_homework) assignType = type.ASSIGN_HOMEWORK;
        if(assign == R.id.assign_test) assignType = type.ASSIGN_TEST;
    }

    public Date getDueDate(){
        return dueDate;
    }
}
