package com.example.scheduler;

public class Course {

    private String className;
    private String professor;
    private String classroom;
    private int schedule;

    public Course(String name, String prof, String room, int sched){
        this.className = name;
        this.professor = prof;
        this.classroom = room;
        this.schedule = sched;
    }
}
