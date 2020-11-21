package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AssignmentAdapter extends ArrayAdapter<Assignment> {

    public Context context;

    public AssignmentAdapter(Context c, ArrayList<Assignment> assignments){
        super(c, 0, assignments);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        Assignment assign = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_assignment_row,
                    parent, false);
        }

        TextView assignName = (TextView) convertView.findViewById(R.id.assign_title);
        TextView assignDue = (TextView) convertView.findViewById(R.id.assign_due);
        TextView description = (TextView) convertView.findViewById(R.id.assign_description);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        assignName.setText(assign.assignName);
        assignDue.setText("Due By: " + formatter.format(assign.dueDate));
        description.setText(assign.description);

        return convertView;
    }
}
