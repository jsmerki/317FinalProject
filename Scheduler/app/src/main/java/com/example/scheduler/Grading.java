package com.example.scheduler;

import java.io.Serializable;
import java.util.ArrayList;

public class Grading implements Serializable {

    public String categoryName;
    public float percentage;
    public ArrayList<Assignment> gradeables;

    public void Grading(String name, float percent){
        this.categoryName = name;
        this.percentage = percent;
        this.gradeables = new ArrayList<Assignment>();
    }

    //Calculate weight
}
