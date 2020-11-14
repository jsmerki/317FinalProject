package com.example.scheduler;

enum type {ASSIGN_READING, ASSIGN_HOMEWORK, ASSIGN_TEST}

public class Assignment {

    private Course course;
    private type assignType;
    private int dueDate;
    private float dueTime;

    public Assignment(type assign, int date, float time){
        this.assignType = assign;
        this.dueDate = date;
        this.dueTime = time;
    }
}
