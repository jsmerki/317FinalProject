package com.example.scheduler;

public class Course {

    public String className;
    public String professor;
    public String classroom;
    public String scheduleStr;
    public int schedule;

    public Course(String name, String prof, String room, String sched){
        this.className = name;
        this.professor = prof;
        this.classroom = room;
        this.scheduleStr = sched;
    }
}
