/*
 * @author: Jacob Merki
 * @description: This file defines the GradingAdapter class that extends the ArrayAdapter class
 * in order to display the list of grading categories that the selected course has as well as
 * performance in each category.
 */
package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * This class defines the way in which grading categories are viewed in the ListView by displaying
 * their name and percentage earned out of total percentage worth.
 */
public class GradingAdapter extends ArrayAdapter<Grading> {

    //Keep track of the array of categories seprately, ran into trouble when trying to use normally
    public ArrayList<Grading> gradeCategories = new ArrayList<Grading>();

    /*
     * This constructor calls the super constructor of the ArrayAdapter class by passing it the
     * array of categories and the context of the app.
     *
     * This method takes Context, ArrayList and returns a GradingAdapter.
     */
    public GradingAdapter(Context c, ArrayList<Grading> grades){
        super(c, 0, grades);
    }

    /*
     * This method updates the array of grading categories that are being manually monitored
     */
    public void updateGradeCategories(ArrayList<Grading> grades){
        this.gradeCategories = grades;
    }

    /*
     * Overridden method to get count of separately monitored array list
     */
    @Override
    public int getCount() {
        return this.gradeCategories.size();
    }

    /*
     * This method constructs the view for individual categories in the list by setting the text
     * to be displayed for each of the views.
     *
     * This method takes int, View and ViewGroup and returns a View.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get grading category and inflate view if necessary
        Grading grading = gradeCategories.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_grading_row,
                    parent, false);
        }

        //Get the TextViews and sent grading name and score
        TextView gradeName = (TextView) convertView.findViewById(R.id.list_grade_name);
        TextView gradeScore = (TextView) convertView.findViewById(R.id.list_grade_score);

        final DecimalFormat twoPlaces = new DecimalFormat("0.00");
        gradeName.setText(grading.categoryName);
        gradeScore.setText(twoPlaces.format(grading.score) + "%/" + grading.percentage + "%");

        return convertView;
    }

}
