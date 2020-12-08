/*
 * @author: Jacob Merki
 * @description: This file defines the Grading class that keeps track of graded assignments that
 * belong to the category and maintains the overall score of the category based on the category's
 * weight of the overall course grade.
 */
package com.example.scheduler;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * The Grading class models individual portions of an overall course grade, made up of assignments
 * and making up a portion of the overlal 100% of the course grade/
 */
public class Grading implements Serializable {

    /*
     * Attributes for the course name, percentage of the course grade, total score and the list
     * of gradeable assignments.
     */
    public String categoryName;
    public float percentage;
    public float score;
    public ArrayList<Assignment> gradeables;

    /*
     * This constructor creates a new Grading object by setting the name and percent worth and
     * initializing the other values.
     *
     * This method takes String, float and returns a Grading object.
     */
    public Grading(String name, float percent){
        this.categoryName = name;
        this.percentage = percent;
        this.score = 0.0f;
        this.gradeables = new ArrayList<Assignment>();
    }

    /*
     * This method adds a newly graded assignment to the Grading's list of gradeables.
     *
     * This method takes an Assignment and returns nothing.
     */
    public void addAssignment(Assignment graded){
        this.gradeables.add(graded);
    }

    /*
     * This method updates the Grading objects overall score and also returns the score to be used
     * elsewhere.
     *
     * This method takes nothing and returns the float calculated score.
     */
    public float getGradingScore(){
        if(gradeables.size() == 0){
            return 0.0f;
        }

        float totalPointsEarned = 0.0f;
        float totalMaxPoints = 0.0f;

        for(Assignment graded: gradeables){
            totalPointsEarned += graded.pointsEarned;
            totalMaxPoints += graded.pointsOutOf;
        }

        this.score = (totalPointsEarned / totalMaxPoints) * percentage;
        System.out.println("New Score For " + this.categoryName + ": " + score + "%");

        return score;
    }

    /*
     * Simple toString override that returns category name for Grading
     */
    @NonNull
    @Override
    public String toString() {
        return this.categoryName;
    }
}
