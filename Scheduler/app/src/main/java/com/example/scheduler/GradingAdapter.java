package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GradingAdapter extends ArrayAdapter<Grading> {

    public ArrayList<Grading> gradeCategories = new ArrayList<Grading>();

    public GradingAdapter(Context c, ArrayList<Grading> grades){
        super(c, 0, grades);
    }

    public void updateGradeCategories(ArrayList<Grading> grades){
        this.gradeCategories = grades;
    }

    @Override
    public int getCount() {
        return this.gradeCategories.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Grading grading = gradeCategories.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_grading_row,
                    parent, false);
        }

        TextView gradeName = (TextView) convertView.findViewById(R.id.list_grade_name);
        TextView gradeScore = (TextView) convertView.findViewById(R.id.list_grade_score);

        final DecimalFormat twoPlaces = new DecimalFormat("0.00");
        gradeName.setText(grading.categoryName);
        gradeScore.setText(twoPlaces.format(grading.score) + "%/" + grading.percentage + "%");

        return convertView;
    }

}
