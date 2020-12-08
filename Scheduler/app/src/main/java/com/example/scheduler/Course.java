/*
 * @author: Jacob Merki
 * @description: This file defines the Course class which keeps track of all assignments and grading
 * categories belonging to the course as well as unique identifying info and professor name and
 * email to be displayed.
 */
package com.example.scheduler;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/*
 * This class defines the Course class which has a name, professor, professor email, classroom,
 * schedule string, an integer for days the course occurs and a list
 */
public class Course implements Serializable {

    //Keep track of identifying course and professor info, and assignments and grading categories
    public String className;
    public String professor;
    public String profEmail;
    public String classroom;
    public String scheduleStr;
    public int courseDays;
    ArrayList<Assignment> allAssignments = new ArrayList<Assignment>();
    ArrayList<Grading> gradingCategories = new ArrayList<Grading>();

    /*
     * This constructor takes all of the necessary information for a new course and sets them to
     * the corresponding attributes.
     *
     * This method takes String, String, String, String, String, int and returns a Course.
     */
    public Course(String name, String prof, String email, String room, String sched, int days){
        this.className = name;
        this.professor = prof;
        this.profEmail = email;
        this.classroom = room;
        this.scheduleStr = sched;
        this.courseDays = days;
    }

    /*
     * These getters return the ArrayLists for the ungraded Assignments and Grading categories
     * that make up a course.
     *
     * The first getter returns ArrayList<Assignment>.
     * The second getter returns ArrayList<Grading>.
     */
    public ArrayList<Assignment> getAssignments(){
        return allAssignments;
    }
    public ArrayList<Grading> getGradingCategories() {
        return gradingCategories;
    }

    /*
     * This method adds a new assignment to the list in order of due date.
     *
     * This method takes a new Assignment and returns a boolean.
     */
    public boolean addAssignment(Assignment newAssignment){
        if(allAssignments.size() == 0){
            allAssignments.add(newAssignment);
        }
        else{
            for(int i = 0; i < allAssignments.size(); i++){
                Assignment temp = allAssignments.get(i);
                int comparison = newAssignment.getDueDate().compareTo(temp.getDueDate());
                //New assignment due date is before or the same as current one
                if(comparison < 0){
                    allAssignments.add(i, newAssignment);
                }
            }
            allAssignments.add(newAssignment);
        }

        return true;
    }

    /*
     * This method returns a reference to the assignment that has a matching name and description.
     *
     * This method takes a String name and a String desricption of the Assignment object and returns
     * the matching assignment.
     */
    public Assignment findAssignment(String name, String descr){
        for(int i = 0; i < allAssignments.size(); i++){
            if(allAssignments.get(i).assignName.equals(name) &&
                    allAssignments.get(i).description.equals(descr)){
                return allAssignments.get(i);
            }
        }
        return null;
    }

    /*
     * This method removes an assignment from the list based on matching name and description if
     * its been deleted or if it will be added to a Grading object.
     *
     * This method takes a String name and a String desricption of the Assignment object and returns
     * a boolean.
     */
    public boolean removeAssignment(String name, String descr){
        System.out.println("Deleting Assignment " + name + " for " + className);
        for(int i = 0; i < allAssignments.size(); i++){
            if(allAssignments.get(i).assignName.equals(name) &&
                    allAssignments.get(i).description.equals(descr)){
                allAssignments.remove(i);
                return true;
            }
        }

        return false;
    }

    /*
     * This method adds a new Grading category to the course.
     *
     * This method takes a Grading and returns nothing.
     */
    public void addGrading(Grading newGrading){
        gradingCategories.add(newGrading);
    }

    /*
     * This method adds a graded assignment to the grading category with the matching name and
     * updates the score of the category.
     *
     * This method takes a String name of the category and the graded Assignment and returns
     * nothing.
     */
    public void addGradedAssignment(String gradingName, Assignment graded){
        for(Grading category: gradingCategories){
            if(category.categoryName.equals(gradingName)){
                //Add assignment and update overall grade for category
                category.addAssignment(graded);
                category.getGradingScore();
            }
        }
    }

    //Overridden toString that returns the name of the Course
    @NonNull
    @Override
    public String toString() {
        return className;
    }
}
