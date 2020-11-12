package com.example.scheduler;

public class Course {

    public String className;
    public String professor;
    public String classroom;
    public int schedule;

    public Course(String name, String prof, String room, int sched){
        this.className = name;
        this.professor = prof;
        this.classroom = room;
        this.schedule = sched;
    }
}
