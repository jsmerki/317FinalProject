/*
 * @author: Jacob Merki
 * @description: This file defines the CourseNotificationService class. This class runs in the
 * background of the phone's operation after the app has been exited and monitors for upcoming
 * classes every 5 minutes. If a class' start time occurs in 15 minutes or a less a notification
 * will be posted with the name of the class and its start time.
 */
package com.example.scheduler;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Date;


/*
 * This class defines the IntentService that monitors for upcoming classes when the app is no longer
 * in the foreground. The app sleeps for five minutes then checks for upcoming classes, posting
 * notifications if a class is starting within the next 15 minutes.
 */
public class CourseNotificationService extends IntentService {

    /*
     * This constructor creates a new CourseNotificationService by calling the super constructor
     * of the IntentService class.
     *
     * This method takes nothing and returns a CourseNotificationService.
     */
    public CourseNotificationService(){ super("CourseNotificationService"); }

    /*
     * These attributes have constants for the nimber of minutes in millis and keep track of the
     * status of the service and the notification channel info.
     */
    public final long FIFTEEN_MINS_MILLIS = 900000;
    public final long FIVE_MINS_MILLIS = 300000;
    public final String NOTIF_CHANNEL_ID = "NOTIFICATION_Scheduler_Course";
    public boolean running = false;
    private NotificationChannel notifChannel;
    private static int notificationId = 0;

    /*
     * This method creates the CourseNotificationService and sets up the notification channel if the
     * user has a high enough API.
     *
     * This method takes nothing and returns nothing.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        running = true;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = getString(R.string.notification_channel);
            String channelDescr = getString(R.string.notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            notifChannel = new NotificationChannel(NOTIF_CHANNEL_ID,
                    channelName, importance);
            notifChannel.setDescription(channelDescr);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notifChannel);
        }
    }

    /*
     * This method handles the intents that start the service. The method gets the courses from the
     * extras and begins running the main loop sequence where it determines the difference between
     * the current time and each of the courses. If a course is starting within the next fifteen
     * minutes a notification fires that displays the name of the course and the starting time.
     *
     * This method takes an Intent and returns nothing.
     */
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
                    //Take the integer of days the course occurs and convert to a string, get
                    //today's time
                    String courseDays = Integer.toString(course.courseDays);
                    Date today = new Date();

                    int startHour = -1, startMin = -1;

                    //If a course occurs today start looking at times
                    if(courseDays.contains(Integer.toString(today.getDate()))){
                        //Get time information from course schedule string
                        String[] schedInfo = course.scheduleStr.split(" ");
                        String startTime = "";
                        for(int i = 0; i < schedInfo.length; i++){
                            if(schedInfo[i].contains(":")){
                                String[] timeInfo = schedInfo[i].split(":");

                                //Only care about start time, so only set if times are still -1
                                if(startHour == -1 || startMin == -1){
                                    startHour = Integer.parseInt(timeInfo[0]);
                                    startMin = Integer.parseInt(timeInfo[1]);

                                    if(schedInfo[i + 1].equals("PM") && startHour != 12){
                                        startHour += 12;
                                    }

                                    //Reconstruct starting time info
                                    startTime = schedInfo[i] + schedInfo[i + 1];
                                }
                            }
                        }

                        //Create a date for the course schedule and get the difference in millis
                        Date courseStart = new Date(today.getYear(), today.getMonth(),
                                today.getDate(), startHour, startMin);
                        long millisDiff = (courseStart.getTime() - today.getTime());

                        //Course is coming up in 15 minutes or less
                        if(millisDiff >= 0 && millisDiff <= FIFTEEN_MINS_MILLIS){
                            String title = getString(R.string.notification_title);
                            String baseNotif = getString(R.string.notification_base);
                            //Build the notification and notify the system
                            NotificationCompat.Builder notifBuilder =
                                new NotificationCompat.Builder(getApplicationContext(),
                                        NOTIF_CHANNEL_ID)
                                    .setSmallIcon(R.drawable.school)
                                    .setContentTitle(title)
                                    .setContentText(baseNotif + " " + course.className + " at " +
                                            startTime)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat managerCompat =
                                    NotificationManagerCompat.from(getApplicationContext());
                            managerCompat.notify(notificationId++, notifBuilder.build());
                        }
                    }
                }

                Thread.sleep(FIVE_MINS_MILLIS);

            } catch (Exception e) { e.printStackTrace(); }

        }

        this.stopSelf();
    }

    /*
     * This method destroys the service and sets the running boolean to false to end the loop.
     *
     * This method takes nothing and returns nothing.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
