package com.example.scheduler;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    public String className;
    public String professor;
    public String profEmail;
    public String classroom;
    public String scheduleStr;
    ArrayList<Assignment> allAssignments = new ArrayList<Assignment>();
    ArrayList<Grading> gradingCategories = new ArrayList<Grading>();

    public Course(String name, String prof, String email, String room, String sched){
        this.className = name;
        this.professor = prof;
        this.profEmail = email;
        this.classroom = room;
        this.scheduleStr = sched;
    }

    public ArrayList<Assignment> getAssignments(){
        return allAssignments;
    }

    public boolean addAssignment(Assignment newAssignment){
        if(allAssignments.size() == 0){
            allAssignments.add(newAssignment);
            return true;
        }
        else{
            for(int i = 0; i < allAssignments.size(); i++){
                Assignment temp = allAssignments.get(i);
                int comparison = newAssignment.getDueDate().compareTo(temp.getDueDate());
                //New assignment due date is before or the same as current one
                if(comparison <= 0){
                    allAssignments.add(i, newAssignment);
                    return true;
                }
            }
            allAssignments.add(newAssignment);
            return true;
        }
    }

    public Assignment findAssignment(String name, String descr){
        for(int i = 0; i < allAssignments.size(); i++){
            if(allAssignments.get(i).assignName.equals(name) &&
                    allAssignments.get(i).description.equals(descr)){
                System.out.println("Assignment found");
                return allAssignments.get(i);
            }
        }
        return null;
    }

    public boolean removeAssignment(String name, String descr){
        System.out.println("Deleting Assignment " + name + " for " + className);
        for(int i = 0; i < allAssignments.size(); i++){
            if(allAssignments.get(i).assignName.equals(name) &&
                    allAssignments.get(i).description.equals(descr)){
                System.out.println("Assignment deleted");
                allAssignments.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addGrading(Grading newGrading){
        gradingCategories.add(newGrading);
    }

    public void addGradedAssignment(String gradingName, Assignment graded){
        for(Grading category: gradingCategories){
            if(category.categoryName.equals(gradingName)){
                category.addAssignment(graded);
                category.getGradingScore();
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return className;
    }
}
