package com.example.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    final String COURSES_FILE_NAME = "course_info.ser";

    public CoursesFragment coursesFrag = new CoursesFragment(this);
    public ScheduleFragment schedFrag = new ScheduleFragment(this);
    public AddCourseFragment addFrag;
    public AddAssignmentFragment assignFrag;
    public EditCourseFragment editFrag;
    public GradingFragment gradesFrag;

    int fragContainerID = R.id.fragment_container;

    public MainActivity getMainActivityInstance(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        //setContentView(R.layout.activity_main);

        final File coursesInfo = new File(getFilesDir(), COURSES_FILE_NAME);
        final SchedulerViewModel model =
                ViewModelProviders.of(this).get(SchedulerViewModel.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Read serialized course info
                if(coursesInfo != null) {
                    System.out.println("File exists");
                    try {
                        FileInputStream coursesInStream = new FileInputStream(coursesInfo);
                        ObjectInputStream coursesIn = new ObjectInputStream(coursesInStream);
                        ArrayList<Course> readCourses = (ArrayList<Course>) coursesIn.readObject();
                        model.setCourses(readCourses);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //Run a 1 second loading animation, should be enough to pull courses from background thread
        //All other setup code runs after animation finishes
        ImageView loadImage = (ImageView) findViewById(R.id.load_image);
        ObjectAnimator loadAnim = ObjectAnimator.ofFloat(loadImage, "rotation", 0f, 360f);
        loadAnim.setDuration(500);
        loadAnim.setRepeatCount(2);
        loadAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        loadAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                setContentView(R.layout.activity_main);

                //Set GradingFragments courses list
                gradesFrag = new GradingFragment(MainActivity.this, model.getCourses());

                if(!isTabletScreen()) {
                    //Set listener for bottom nav buttons
                    BottomNavigationView navView = findViewById(R.id.navigation);
                    navView.setOnNavigationItemSelectedListener(new MenuNavListener());

                    FragmentTransaction coursesTransaction = getSupportFragmentManager().beginTransaction();
                    coursesTransaction.add(R.id.fragment_container, coursesFrag);
                    //coursesTransaction.addToBackStack(null);
                    coursesTransaction.commit();
                }

                else{
                    setContentView(R.layout.activity_main_tablet);

                    BottomNavigationView navViewTab = findViewById(R.id.navigation_tablet);
                    navViewTab.setOnNavigationItemSelectedListener(new TabletMenuNavListener());

                    FragmentTransaction displaySched = getSupportFragmentManager().beginTransaction();
                    displaySched.add(R.id.schedule_frag_tablet, schedFrag);
                    displaySched.commit();

                    FragmentTransaction displayCourses = getSupportFragmentManager().beginTransaction();
                    displayCourses.add(R.id.fragment_container_tablet, coursesFrag);
                    displayCourses.commit();

                    fragContainerID = R.id.fragment_container_tablet;
                }
            }
        });
        loadAnim.start();

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        //Stop the notifications intent when in the app, user can see their schedule
        Intent notificationsIntent = new Intent(this, CourseNotificationService.class);
        stopService(notificationsIntent);
    }

    public boolean isTabletScreen(){
        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);
        float widthIn = (float) (screen.widthPixels / screen.xdpi);
        float heightIn = (float) (screen.heightPixels / screen.xdpi);
        return widthIn > 5 && heightIn > 5;
    }

    public void insertAddCourseFragment(View view){
        addFrag = new AddCourseFragment(this);

        FragmentTransaction addCourseTransaction = getSupportFragmentManager().beginTransaction();
        addCourseTransaction.setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out
        );
        addCourseTransaction.replace(fragContainerID, addFrag);
        addCourseTransaction.addToBackStack(null);
        addCourseTransaction.commit();
    }

    public void returnToCoursesFragment(){
        FragmentTransaction removeAddCourseTransaction =
                getSupportFragmentManager().beginTransaction();
        removeAddCourseTransaction.setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out
        );
        removeAddCourseTransaction.replace(fragContainerID, coursesFrag);
        //removeAddCourseTransaction.addToBackStack(null);
        removeAddCourseTransaction.commit();
    }

    public void insertEditCoursesFragment(Course editCourse){
        editFrag = new EditCourseFragment(this, editCourse);

        FragmentTransaction editCourseTransaction = getSupportFragmentManager().beginTransaction();
        editCourseTransaction.setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out
        );
        editCourseTransaction.replace(fragContainerID, editFrag);
        //editCourseTransaction.addToBackStack(null);
        editCourseTransaction.commit();
    }

    public void returnToEditCourseFragment(){
        FragmentTransaction removeAddAssignTransaction =
                getSupportFragmentManager().beginTransaction();
        removeAddAssignTransaction.setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out
        );
        removeAddAssignTransaction.replace(fragContainerID, editFrag);
        //removeAddAssignTransaction.addToBackStack(null);
        removeAddAssignTransaction.commit();
    }

    public void insertAddAssignmentFragment(View view){
        assignFrag = new AddAssignmentFragment(this, editFrag.getCourseToEdit());

        FragmentTransaction addAssignTransaction = getSupportFragmentManager().beginTransaction();
        addAssignTransaction.setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out
        );
        addAssignTransaction.replace(fragContainerID, assignFrag);
        addAssignTransaction.commit();
    }

    //ON CLICK LISTENERS FOR BUTTONS IN EDIT COURSE FRAGMENT
    public void deleteAssignment(View v){
        LinearLayout assignmentRow = (LinearLayout) v.getParent().getParent().getParent();
        TextView assignName = (TextView) assignmentRow.findViewById(R.id.assign_title);
        TextView assignDescr = (TextView) assignmentRow.findViewById(R.id.assign_description);
        editFrag.getCourseToEdit().removeAssignment(assignName.getText().toString(),
                assignDescr.getText().toString());
        editFrag.assignmentsChanged();
    }

    public void showReminderDialog(View v){
        LinearLayout assignmentRow = (LinearLayout) v.getParent().getParent().getParent();
        TextView assignName = (TextView) assignmentRow.findViewById(R.id.assign_title);
        TextView assignDescr = (TextView) assignmentRow.findViewById(R.id.assign_description);

        Assignment assign = editFrag.getCourseToEdit()
                .findAssignment(assignName.getText().toString(), assignDescr.getText().toString());

        CalendarReminderDialog dialog = new CalendarReminderDialog(assign);
        dialog.show(getSupportFragmentManager(), "reminder");
    }

    public void saveInCalendar(Assignment assign, int reminderMinutes){


        System.out.println("Getting Calendars");

        long myCalID = 1;
        Course course = assign.getCourse();
        Calendar startTime = Calendar.getInstance();
        Calendar stopTime = Calendar.getInstance();

        System.out.println("DATE: " + assign.dueDate.getYear() + " " + assign.dueDate.getMonth() + " " +
                assign.dueDate.getDate());

        //Accounting for DatePicker returning date number off by 2
        startTime.set(assign.dueDate.getYear(), assign.dueDate.getMonth(),
                assign.dueDate.getDate(), 0, 0);
        stopTime.set(assign.dueDate.getYear(), assign.dueDate.getMonth(),
                assign.dueDate.getDate(), 0, 5);

        long startMillis = startTime.getTimeInMillis();
        long stopMillis = stopTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues eventVals = new ContentValues();
        eventVals.put(CalendarContract.Events.DTSTART, startMillis);
        eventVals.put(CalendarContract.Events.DTEND, stopMillis);
        eventVals.put(CalendarContract.Events.TITLE, course.className + ": " + assign.assignName);
        eventVals.put(CalendarContract.Events.CALENDAR_ID, myCalID);
        eventVals.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Phoenix");

        if(checkSelfPermission(Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED) {
            Uri eventURI = cr.insert(CalendarContract.Events.CONTENT_URI, eventVals);
            long eventID = Long.parseLong(eventURI.getLastPathSegment());

            //Only add a reminder if user wanted. minutes will be -1 if not
            if(reminderMinutes > 0) {
                ContentValues reminderVals = new ContentValues();
                reminderVals.put(CalendarContract.Reminders.MINUTES, reminderMinutes);
                reminderVals.put(CalendarContract.Reminders.EVENT_ID, eventID);
                reminderVals.put(CalendarContract.Reminders.METHOD,
                        CalendarContract.Reminders.METHOD_ALERT);

                Uri reminderURI = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderVals);
            }
            System.out.println("New Event Created! ID: " + eventID);

            Toast calWriteToast = Toast.makeText(getApplicationContext(), "New Event Created",
                    Toast.LENGTH_SHORT);
            calWriteToast.show();
        }
        else{
            Toast calFailToast = Toast.makeText(getApplicationContext(),
                    "ERROR: Calendar Permissions Needed",
                    Toast.LENGTH_SHORT);
            calFailToast.show();
        }

    }

    public void submitAssignment(View v){
        LinearLayout assignmentRow = (LinearLayout) v.getParent().getParent().getParent();
        TextView assignName = (TextView) assignmentRow.findViewById(R.id.assign_title);
        TextView assignDescr = (TextView) assignmentRow.findViewById(R.id.assign_description);
        //Display dialog that gets score and grading category
        SubmitAssignmentDialog dialog = new SubmitAssignmentDialog(editFrag.getCourseToEdit());
        dialog.setAssignmentInfo(assignName.getText().toString(), assignDescr.getText().toString());
        dialog.show(getSupportFragmentManager(), "submit");
    }

    public void emailProfessor(View v){
        TextView profEmail = (TextView) v;
        Intent emailProf = new Intent(Intent.ACTION_SEND);
        emailProf.setType("vnd.android.cursor.dir/email");

        emailProf.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { profEmail.getText().toString() });

        startActivity(emailProf);
    }

    public void updateAssignmentsAdapter(){
        editFrag.assignmentsChanged();
    }

    //FUNCTIONS FOR NEW GRADING DIALOG
    public void showAddGradingDialog(View v){
        AddGradingDialog dialog = new AddGradingDialog();
        dialog.show(getSupportFragmentManager(), "grading");
    }

    public void addGradingToCourse(Grading grade){
        gradesFrag.addGradingInFragment(grade);
    }

    //Callback for getting weather data in schedule
    public void getWeatherReport(View v){
        new WeatherWebRequest(this).execute();
    }

    public void showWeatherDialog(JSONObject weather){
        WeatherDialog weatherLog = new WeatherDialog(weather);
        weatherLog.show(getSupportFragmentManager(), "weather");

    }

    //Save course information to files
    @Override
    protected void onStop() {

        System.out.println("Stopping app");
        File courseInfo = new File(getFilesDir(), COURSES_FILE_NAME);
        SchedulerViewModel model =
                ViewModelProviders.of(this).get(SchedulerViewModel.class);

        try {
            FileOutputStream coursesOutStream = new FileOutputStream(courseInfo);
            ObjectOutputStream coursesOut = new ObjectOutputStream(coursesOutStream);
            coursesOut.writeObject(model.getCourses());
            coursesOut.close();
            coursesOutStream.close();

            Intent notificationsIntent = new Intent(this, CourseNotificationService.class);
            notificationsIntent.putExtra("COURSES", model.getCourses());
            startService(notificationsIntent);
        }catch(Exception e){ e.printStackTrace(); }



        super.onStop();
    }

    //Listener for bottom navigation bar
    public class MenuNavListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.courses_page){
                FragmentTransaction displayCourses = getSupportFragmentManager().beginTransaction();
                displayCourses.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displayCourses.replace(fragContainerID, coursesFrag);
                displayCourses.commit();

                return true;
            }
            else if(item.getItemId() == R.id.schedule_page){
                FragmentTransaction displaySched = getSupportFragmentManager().beginTransaction();
                displaySched.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displaySched.replace(fragContainerID, schedFrag);
                displaySched.commit();

                return true;
            }
            else if(item.getItemId() == R.id.grading_page){
                FragmentTransaction displayGrades = getSupportFragmentManager().beginTransaction();
                displayGrades.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displayGrades.replace(fragContainerID, gradesFrag);
                displayGrades.commit();

                return true;
            }
            else if(item.getItemId() == R.id.help_page){
                Toast helpToast = Toast.makeText(getApplicationContext(), "HELPING",
                        Toast.LENGTH_SHORT);
                helpToast.show();
                return true;
            }
            return false;
        }
    }

    public class TabletMenuNavListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            System.out.println("Tablet Nav Item Selected");
            if(item.getItemId() == R.id.courses_page_tablet){
                FragmentTransaction displayCourses = getSupportFragmentManager().beginTransaction();
                displayCourses.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displayCourses.replace(fragContainerID, coursesFrag);
                displayCourses.commit();

                return true;
            }
            else if(item.getItemId() == R.id.grading_page_tablet){
                FragmentTransaction displayGrades = getSupportFragmentManager().beginTransaction();
                displayGrades.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displayGrades.replace(fragContainerID, gradesFrag);
                displayGrades.commit();

                return true;
            }
            else if(item.getItemId() == R.id.help_page_tablet){
                Toast helpToast = Toast.makeText(getApplicationContext(), "HELPING",
                        Toast.LENGTH_SHORT);
                helpToast.show();
                return true;
            }
            return false;
        }
    }
}