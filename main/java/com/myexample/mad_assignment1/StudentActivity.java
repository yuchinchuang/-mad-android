package com.myexample.mad_assignment1;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentActivity extends Fragment {

    private DatabaseManager myDbManager;
    private TextView tvTitle;
    private ListView lvStudents;
    private ArrayList<Student> studentList;
    private ImageView btnAddStudent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDbManager = new DatabaseManager(getActivity());
        lvStudents = view.findViewById(R.id.lvStudents);
        tvTitle = view.findViewById(R.id.tvStudentList);
        btnAddStudent = view.findViewById(R.id.imgAddStudent);


        myDbManager.openReadable();
        studentList = myDbManager.getAllStudentRecords();
        tvTitle.setText("Student List (" + studentList.size() + ")");

        final StudentAdapter arrayAdpt = new StudentAdapter(getActivity(), studentList);
        lvStudents.setAdapter(arrayAdpt);

        lvStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student s = (Student)adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), StudentDetailsActivity.class);
                intent.putExtra("student", s.getStudentId());
                startActivity(intent);
            }
        });

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudentDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
