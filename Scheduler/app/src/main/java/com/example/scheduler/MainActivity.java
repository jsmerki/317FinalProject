/*
 * @author: Jacob Merki
 * @description: This app allows users to manage their courses, assignments and grades for a given
 * semester. The user can create instances of courses, add assignments to those courses and submit
 * assignments with grades, which will begin automatically calculating their letter grade based
 * on the grading category for the class.
 * NOTE: Most fragment transactions were not added to the back-stack to maintain the desired flow
 * of navigation.
 */
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

/*
 * This class defines the main activity that the user sees when the app is run on their device. The
 * app starts by displaying the courses menu and the courses that the user has created. The user
 * can move between different menus with a bottom navigation bar to look at their schedule, grade
 * status and look at the help page for the app. The entire app runs in a single activity and
 * incorporates fragments to move between sections of functionality.
 */
public class MainActivity extends AppCompatActivity {



    /*
     * These attributes are used for keeping track of all the different fragments used for courses,
     * scheduling, grading and help.
     */
    public CoursesFragment coursesFrag = new CoursesFragment(this);
    public ScheduleFragment schedFrag = new ScheduleFragment(this);
    public HelpFragment helpFrag = new HelpFragment(this);
    public AddCourseFragment addFrag;
    public AddAssignmentFragment assignFrag;
    public EditCourseFragment editFrag;
    public GradingFragment gradesFrag;

    //Final file name and variable for frag ID which can be switchted to tablet container if needed
    final String COURSES_FILE_NAME = "course_info.ser";
    int fragContainerID = R.id.fragment_container;

    /*
     * This method returns the running instance of the MainActivity class to help with some nested
     * class' need to have an instance of the Mainactivity.
     *
     * This method takes nothing and returns a MainActivity.
     */
    public MainActivity getMainActivityInstance(){
        return this;
    }

    /*
     * This method takes a Bundle object and uses it to create the application's main activity.
     * The activity starts by running a separate thread to read the user's serialized course info
     * from a file, then playing a loading animation while info is retrieved. Once the loading
     * animation ends and courses have been read the layout is set to the appropriate layout file
     * (tablet or phone form factor) and the user's courses (and schedule if on tablet) are
     * displayed.
     *
     * This method takes a Bundle object and returns nothing.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Start with loading animation
        setContentView(R.layout.activity_load);

        //Create File reference to serialized course info and get ViewModel for courses
        final File coursesInfo = new File(getFilesDir(), COURSES_FILE_NAME);
        final SchedulerViewModel model =
                ViewModelProviders.of(this).get(SchedulerViewModel.class);

        //Retrieve courses off of the UI thread so that UI thread can display loading animation
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

        //Run a 2 second loading animation, should be enough to pull courses from background thread
        //All other setup code runs after animation finishes
        ImageView loadImage = (ImageView) findViewById(R.id.load_image);
        ObjectAnimator loadAnim = ObjectAnimator.ofFloat(loadImage, "rotation", 0f, -360f);
        loadAnim.setDuration(500);
        loadAnim.setRepeatCount(3);//3 repeats, 4 cycles total
        loadAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        //Run remaining setup code after animation is complete
        loadAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                setContentView(R.layout.activity_main);

                //Set GradingFragments courses list
                gradesFrag = new GradingFragment(MainActivity.this, model.getCourses());

                //Check for screen size and set the layout and listeners accordingly
                if(!isTabletScreen()) {
                    //Set listener for bottom nav buttons
                    BottomNavigationView navView = findViewById(R.id.navigation);
                    navView.setOnNavigationItemSelectedListener(new MenuNavListener());

                    //Add courses layout
                    FragmentTransaction coursesTransaction = getSupportFragmentManager()
                            .beginTransaction();
                    coursesTransaction.add(R.id.fragment_container, coursesFrag);
                    coursesTransaction.commit();
                }
                //If on a tablet set the layout to the right file and change the fragContainerID
                else{
                    setContentView(R.layout.activity_main_tablet);

                    //Set listener for bottom nav buttons
                    BottomNavigationView navViewTab = findViewById(R.id.navigation_tablet);
                    navViewTab.setOnNavigationItemSelectedListener(new TabletMenuNavListener());

                    //Add schedule along with courses layout
                    FragmentTransaction displaySched = getSupportFragmentManager()
                            .beginTransaction();
                    displaySched.add(R.id.schedule_frag_tablet, schedFrag);
                    displaySched.commit();

                    //Add courses layout
                    FragmentTransaction displayCourses = getSupportFragmentManager().beginTransaction();
                    displayCourses.add(R.id.fragment_container_tablet, coursesFrag);
                    displayCourses.commit();

                    //Change the ID reference used in other transactions
                    fragContainerID = R.id.fragment_container_tablet;
                }
            }
        });
        loadAnim.start();

    }

    /*
     * This method turns off the background service that checks for upcoming classes once the app
     * has been exited or closed.
     *
     * This method takes a nothing and returns nothing.
     */
    @Override
    protected void onRestart(){
        super.onRestart();
        //Stop the notifications intent when in the app, user can see their schedule
        Intent notificationsIntent = new Intent(this, CourseNotificationService.class);
        stopService(notificationsIntent);
    }

    /*
     * This method gets the screen mtrics and checks if the device screen is large enough to be
     * qualified as a tablet (returns true) or as a phone (returns false).
     *
     * This method takes a nothing and returns a boolean.
     */
    public boolean isTabletScreen(){
        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);
        float widthIn = (float) (screen.widthPixels / screen.xdpi);
        float heightIn = (float) (screen.heightPixels / screen.xdpi);
        return widthIn > 5 && heightIn > 5;
    }

    /*
     * This method is a listener for the "+" button in the CoursesFragment that replaces the
     * CoursesFragment with an AddCoursesFragment that represents a form of course information.
     *
     * This method takes the View that called it and returns nothing.
     */
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

    /*
     * This method is called from AddCourseFragment after a course has been successfully registered
     * and added to the ViewModel. This method navigates the user back to the CourseFragment.
     *
     * This method takes nothing and returns nothing.
     */
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
        removeAddCourseTransaction.commit();
    }

    /*
     * This method is called from CoursesFragment when a course in the ListView is clicked on. This
     * method replaces the CoursesFragment with an EditCourseFragment where the user can add
     * assignments to their courses.
     *
     * This method takes the Course being edited and returns nothing.
     */
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
        editCourseTransaction.commit();
    }

    /*
     * This method is a listener for the "+" button in the EditCourseFragment that navigates the
     * user to an AddAssignmentFragment where the user can fill out a form for a new assignment
     * for the course being edited.
     *
     * This method takes the View that called it and returns nothing.
     */
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

    /*
     * This method is called from AddAssignmentFragment when the user has finished creating the
     * new assignment for the course and navigates them back to the EditCourseFragment with the
     * same course still selected.
     *
     * This method takes nothing and returns nothing.
     */
    public void returnToEditCourseFragment(){
        //NOTE: By using the same editFrag instance the class being edited remains the same
        FragmentTransaction removeAddAssignTransaction =
                getSupportFragmentManager().beginTransaction();
        removeAddAssignTransaction.setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out
        );
        removeAddAssignTransaction.replace(fragContainerID, editFrag);
        removeAddAssignTransaction.commit();
    }

    //ON CLICK LISTENERS FOR BUTTONS IN EDIT COURSE FRAGMENT
    //ASSIGNMENTADAPTER VIEWS HAVE 3 BUTTONS TO INTERACT WITH ASSIGNMENTS

    /*
     * This method is called when a user clicks the trash can icon on an assignment in the
     * EditCourseFragment, which deletes the assignment from the course list of assignments.
     *
     * This method takes the View that called it and returns nothing.
     */
    public void deleteAssignment(View v){
        //Get assignment info
        LinearLayout assignmentRow = (LinearLayout) v.getParent().getParent().getParent();
        TextView assignName = (TextView) assignmentRow.findViewById(R.id.assign_title);
        TextView assignDescr = (TextView) assignmentRow.findViewById(R.id.assign_description);
        //Assignments are removed by identifying with name and description
        editFrag.getCourseToEdit().removeAssignment(assignName.getText().toString(),
                assignDescr.getText().toString());
        editFrag.assignmentsChanged();
    }

    /*
     * This method is called when a user clicks the calendar icon on an assignment in the
     * EditCourseFragment which shows a dialog where the user can configure a reminder for the
     * event being added to their calendar.
     *
     * This method takes the View that called it and returns nothing.
     */
    public void showReminderDialog(View v){
        //Get assignment info
        LinearLayout assignmentRow = (LinearLayout) v.getParent().getParent().getParent();
        TextView assignName = (TextView) assignmentRow.findViewById(R.id.assign_title);
        TextView assignDescr = (TextView) assignmentRow.findViewById(R.id.assign_description);

        Assignment assign = editFrag.getCourseToEdit()
                .findAssignment(assignName.getText().toString(), assignDescr.getText().toString());

        CalendarReminderDialog dialog = new CalendarReminderDialog(assign);
        dialog.show(getSupportFragmentManager(), "reminder");
    }

    /*
     * This method is called after the user has finished with the reminder dialog, and uses the
     * Calendar ContentProvider to create a new event with the desired reminder amount if the user
     * chose one.
     *
     * This method takes the Assignment to save, an int of minutes for the reminder and returns
     * nothing.
     */
    public void saveInCalendar(Assignment assign, int reminderMinutes){

        //IDefault Calendar ID that works for both Android and Google phones
        long myCalID = 2;
        Course course = assign.getCourse();
        Calendar startTime = Calendar.getInstance();
        Calendar stopTime = Calendar.getInstance();

        //Set the start and stop time from 12:00AM-12:05AM on assignment due date
        //Currently assignments don't have a due time option
        startTime.set(assign.dueDate.getYear(), assign.dueDate.getMonth(),
                assign.dueDate.getDate(), 0, 0);
        stopTime.set(assign.dueDate.getYear(), assign.dueDate.getMonth(),
                assign.dueDate.getDate(), 0, 5);


        long startMillis = startTime.getTimeInMillis();
        long stopMillis = stopTime.getTimeInMillis();

        //Create the basic event info for assignment
        ContentResolver cr = getContentResolver();
        ContentValues eventVals = new ContentValues();
        eventVals.put(CalendarContract.Events.DTSTART, startMillis);
        eventVals.put(CalendarContract.Events.DTEND, stopMillis);
        eventVals.put(CalendarContract.Events.TITLE, course.className + ": " + assign.assignName);
        eventVals.put(CalendarContract.Events.CALENDAR_ID, myCalID);
        eventVals.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Phoenix");

        //Determine if permission have been allowed, if not show an error Toast
        //This code still runs on Google calendars which don't seem to be a part of the Android
        //Calendar ContentProvider, addressable in the future
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

    /*
     * This method is called when a user clicks the checkmark icon on an assignment in the
     * EditCourseFragment which shows a dialog where the user provides the score they got on an
     * assignment and what grading category the assignment will belong to.
     *
     * This method takes the View that called it and returns nothing.
     */
    public void submitAssignment(View v){
        //Get assignment info
        LinearLayout assignmentRow = (LinearLayout) v.getParent().getParent().getParent();
        TextView assignName = (TextView) assignmentRow.findViewById(R.id.assign_title);
        TextView assignDescr = (TextView) assignmentRow.findViewById(R.id.assign_description);

        Course course = editFrag.getCourseToEdit();

        if(course.gradingCategories.size() > 0){
            //Display dialog that gets score and grading category
            SubmitAssignmentDialog dialog = new SubmitAssignmentDialog(editFrag.getCourseToEdit());
            dialog.setAssignmentInfo(assignName.getText().toString(),
                    assignDescr.getText().toString());
            dialog.show(getSupportFragmentManager(), "submit");
        }
        //If the user hasn't created a grading category for the course, don't submit the assignment
        else{
            Toast submitFailToast = Toast.makeText(getApplicationContext(),
                    "ERROR: Add Grading Categories First!",
                    Toast.LENGTH_SHORT);
            submitFailToast.show();
        }

    }

    /*
     * This method is a listener for the email TextView in the EditCourseFragment that opens an
     * email with the professor's email in the TO: line via implicit intent.
     *
     * This method takes the View that called it and returns nothing.
     */
    public void emailProfessor(View v){
        TextView profEmail = (TextView) v;
        Intent emailProf = new Intent(Intent.ACTION_SEND);
        emailProf.setType("vnd.android.cursor.dir/email");

        emailProf.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { profEmail.getText().toString() });

        startActivity(emailProf);
    }

    /*
     * This method notifies the EditCourseFragment of the assignment list being updated. This
     * method exists due to scoping and method access issues.
     *
     * This method takes nothing and returns nothing.
     */
    public void updateAssignmentsAdapter(){
        editFrag.assignmentsChanged();
    }

    //FUNCTIONS FOR NEW GRADING DIALOG

    /*
     * This method is a listener for the "+" button in the GradingFragment that shows a dialog to
     * add a new Grading object to the course selected by the Spinner.
     *
     * This method takes the View that called it and returns nothing.
     */
    public void showAddGradingDialog(View v){
        AddGradingDialog dialog = new AddGradingDialog();
        dialog.show(getSupportFragmentManager(), "grading");
    }

    /*
     * This method adds a Grading object to the course selected by the Spinner and exists because
     * of scoping and access issues.
     *
     * This method takes the new Grading and returns nothing.
     */
    public void addGradingToCourse(Grading grade){
        gradesFrag.addGradingInFragment(grade);
    }

    /*
     * This method makes a new AsyncTask that requests the local weather for Tucson, Arizona
     * via a public weather API.
     *
     * This method takes the View that called it and returns nothing.
     */
    public void getWeatherReport(View v){
        new WeatherWebRequest(this).execute();
    }

    /*
     * This method shows a dialog that displays the results of the weather request from the API.
     *
     * This method takes the JSONObject of weather data and returns nothing.
     */
    public void showWeatherDialog(JSONObject weather){
        WeatherDialog weatherLog = new WeatherDialog(weather);
        weatherLog.show(getSupportFragmentManager(), "weather");

    }

    /*
     * This method is called when the app is exited and saves the user's course, assignment and
     * grading data as serialized objects to a file in the app's private internal storage. This
     * method also starts the IntentService that monitors for upcoming classes and posts
     * notifications.
     *
     * This method takes nothing and returns nothing.
     */
    @Override
    protected void onStop() {
        //Get courses from ViewModel and prepare file
        File courseInfo = new File(getFilesDir(), COURSES_FILE_NAME);
        SchedulerViewModel model =
                ViewModelProviders.of(this).get(SchedulerViewModel.class);

        try {
            //Save course info over ObjectOutputStream
            FileOutputStream coursesOutStream = new FileOutputStream(courseInfo);
            ObjectOutputStream coursesOut = new ObjectOutputStream(coursesOutStream);
            coursesOut.writeObject(model.getCourses());
            coursesOut.close();
            coursesOutStream.close();

            //Start intent service after course info has been saved
            Intent notificationsIntent = new Intent(this, CourseNotificationService.class);
            notificationsIntent.putExtra("COURSES", model.getCourses());
            startService(notificationsIntent);
        }catch(Exception e){ e.printStackTrace(); }



        super.onStop();
    }


    /*
     * This class defines the listener for the BottomNavigationView for the app when on a phone.
     * This listener only handles transactions between fragments depending on what menu has been
     * selected.
     */
    public class MenuNavListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        /*
         * This method is called when a new menu item has been selected and creates a transaction
         * that displays the fragment that corresponds to the selected menu item.
         *
         * This method takes the selected MenuItem and returns nothing and returns nothing.
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Show CoursesFragment
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
            //Show ScheduleFragment
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
            //Show GradingFragment
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
            //Show HelpFragment
            else if(item.getItemId() == R.id.help_page){
                FragmentTransaction displayHelp = getSupportFragmentManager().beginTransaction();
                displayHelp.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displayHelp.replace(fragContainerID, helpFrag);
                displayHelp.commit();

                return true;
            }
            return false;
        }
    }

    /*
     * This class defines the listener for the BottomNavigationView for the app when on a tablet.
     * This listener only handles transactions between fragments depending on what menu has been
     * selected.
     */
    public class TabletMenuNavListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        /*
         * This method is called when a new menu item has been selected and creates a transaction
         * that displays the fragment that corresponds to the selected menu item.
         *
         * This method takes the selected MenuItem and returns nothing and returns nothing.
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Show CoursesFragment
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
            //Show GradingFragment
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
            //Show HelpFragment
            else if(item.getItemId() == R.id.help_page_tablet){
                FragmentTransaction displayHelp = getSupportFragmentManager().beginTransaction();
                displayHelp.setCustomAnimations(
                        R.anim.fragment_slide_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_slide_out
                );
                displayHelp.replace(fragContainerID, helpFrag);
                displayHelp.commit();
                return true;
            }
            return false;
        }
    }
}