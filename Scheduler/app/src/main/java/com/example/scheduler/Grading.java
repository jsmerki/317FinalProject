package com.example.scheduler;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Grading implements Serializable {

    public String categoryName;
    public float percentage;
    public float score;
    public ArrayList<Assignment> gradeables;

    public Grading(String name, float percent){
        this.categoryName = name;
        this.percentage = percent;
        this.score = 0.0f;
        this.gradeables = new ArrayList<Assignment>();
    }

    public void addAssignment(Assignment graded){
        this.gradeables.add(graded);
    }
    //Calculate weight
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

    @NonNull
    @Override
    public String toString() {
        return this.categoryName;
    }
}
