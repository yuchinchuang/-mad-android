package com.myexample.mad_assignment1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student>{

    private Context context;
    private ArrayList<Student> students;

    public StudentAdapter(@NonNull Context context, ArrayList<Student> students) {
        super(context, R.layout.rowlayout_student, students);

        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout_student, parent, false);

        TextView tvSId = rowView.findViewById(R.id.tvSId_srow);
        TextView tvSName = rowView.findViewById(R.id.tvSName_srow);

        Student student = students.get(position);
        String sName = student.getsFirstName() + " " + student.getsLastName();

        tvSId.setText(student.getStudentId());
        tvSName.setText(sName);

        return rowView;
    }

}
