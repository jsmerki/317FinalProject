package com.example.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String COURSES_FILE_NAME = "course_info.ser";

    public CoursesFragment coursesFrag = new CoursesFragment(this);
    public AddCourseFragment addFrag = new AddCourseFragment(this);
    public AddAssignmentFragment assignFrag;
    public EditCourseFragment editFrag;
    public GradingFragment gradesFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set listener for bottom nav buttons
        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(new MenuNavListener());

        File coursesInfo = new File(getFilesDir(), COURSES_FILE_NAME);
        SchedulerViewModel model =
                ViewModelProviders.of(this).get(SchedulerViewModel.class);

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

        //Set GradingFragments courses list
        gradesFrag = new GradingFragment(this, model.getCourses());

        FragmentTransaction coursesTransaction = getSupportFragmentManager().beginTransaction();
        coursesTransaction.add(R.id.fragment_container, coursesFrag);
        //coursesTransaction.addToBackStack(null);
        coursesTransaction.commit();
    }

    public void insertAddCourseFragment(View view){

        FragmentTransaction addCourseTransaction = getSupportFragmentManager().beginTransaction();
        addCourseTransaction.replace(R.id.fragment_container, addFrag);
        addCourseTransaction.addToBackStack(null);
        addCourseTransaction.commit();
    }

    public void returnToCoursesFragment(){
        FragmentTransaction removeAddCourseTransaction =
                getSupportFragmentManager().beginTransaction();
        removeAddCourseTransaction.replace(R.id.fragment_container, coursesFrag);
        //removeAddCourseTransaction.addToBackStack(null);
        removeAddCourseTransaction.commit();
    }

    public void insertEditCoursesFragment(Course editCourse){
        editFrag = new EditCourseFragment(this, editCourse);

        FragmentTransaction editCourseTransaction = getSupportFragmentManager().beginTransaction();
        editCourseTransaction.replace(R.id.fragment_container, editFrag);
        //editCourseTransaction.addToBackStack(null);
        editCourseTransaction.commit();
    }

    public void returnToEditCourseFragment(){
        FragmentTransaction removeAddAssignTransaction =
                getSupportFragmentManager().beginTransaction();
        removeAddAssignTransaction.replace(R.id.fragment_container, editFrag);
        //removeAddAssignTransaction.addToBackStack(null);
        removeAddAssignTransaction.commit();
    }

    public void insertAddAssignmentFragment(View view){
        assignFrag = new AddAssignmentFragment(this, editFrag.getCourseToEdit());

        FragmentTransaction addAssignTransaction = getSupportFragmentManager().beginTransaction();
        addAssignTransaction.replace(R.id.fragment_container, assignFrag);
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

    //FUNCTIONS FOR NEW GRADING DIALOG
    public void showAddGradingDialog(View v){
        AddGradingDialog dialog = new AddGradingDialog();
        dialog.show(getSupportFragmentManager(), "grading");
    }

    public void addGradingToCourse(Grading grade){
        gradesFrag.addGradingInFragment(grade);
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
        }catch(Exception e){ e.printStackTrace(); }

        super.onStop();
    }

    //Listener for bottom navigation bar
    public class MenuNavListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.courses_page){
                FragmentTransaction displayCourses = getSupportFragmentManager().beginTransaction();
                displayCourses.replace(R.id.fragment_container, coursesFrag);
                displayCourses.commit();

                return true;
            }
            else if(item.getItemId() == R.id.schedule_page){
                return true;
            }
            else if(item.getItemId() == R.id.grading_page){
                FragmentTransaction displayGrades = getSupportFragmentManager().beginTransaction();
                displayGrades.replace(R.id.fragment_container, gradesFrag);
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
}