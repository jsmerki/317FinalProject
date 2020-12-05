package com.example.scheduler;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


public class CourseNotificationService extends IntentService {

    public CourseNotificationService(){ super("CourseNotificationService"); }

    final long FIFTEEN_MINS_MILLIS = 900000;
    final long FIVE_MINS_MILLIS = 300000;
    boolean running = false;

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        if(extras == null){
            return;
        }

        ArrayList<Course> courses = (ArrayList<Course>) extras.get("COURSES");

        while(running){

            try {

                for(Course course: courses){
                    String courseDays = Integer.toString(course.courseDays);
                    Date today = new Date();

                    int startHour = -1, startMin = -1;

                    if(courseDays.contains(Integer.toString(5))){
                        //Compare times
                        String[] schedInfo = course.scheduleStr.split(" ");

                        for(int i = 0; i < schedInfo.length; i++){
                            if(schedInfo[i].contains(":")){
                                String[] timeInfo = schedInfo[i].split(":");

                                if(startHour == -1 || startMin == -1){
                                    startHour = Integer.parseInt(timeInfo[0]);
                                    startMin = Integer.parseInt(timeInfo[1]);

                                    if(schedInfo[i + 1].equals("PM") && startHour != 12){
                                        startHour += 12;
                                    }
                                }
                            }
                        }

                        Date courseStart = new Date(today.getYear(), today.getMonth(), today.getDate(), startHour, startMin);
                        long millisDiff = (courseStart.getTime() - today.getTime());

                        //Course is coming up in 15 minutes or less
                        if(millisDiff >= 0 && millisDiff <= FIFTEEN_MINS_MILLIS){
                            //FIXME: MAKE AND SHOW NOTIFICATION
                            System.out.println("CLASS COMING UP: " + course.className);
                        }
                    }
                }

                Thread.sleep(FIVE_MINS_MILLIS);

            } catch (Exception e) { e.printStackTrace(); }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
