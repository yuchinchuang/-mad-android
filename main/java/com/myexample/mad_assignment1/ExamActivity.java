package com.myexample.mad_assignment1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ExamActivity extends AppCompatActivity {

    private DatabaseManager myDbManager;
    private TextView tvTitle;
    private ListView lvExams;
    private ExamAdapter arrayAdpt;
    private ArrayList<Exam> examList;
    private ImageView btnAddExam;

    private TableLayout tblExamDetail;
    private EditText etUnitName;
    private EditText etEDate;
    private EditText etETime;
    private EditText etLocation;
    private Button btnAddNewExam;
    private Button btnBackToExamList;
    private Button btnSelectDate;
    private Button btnSelectTime;
    private ImageView btnDeleteExam;

    private LinearLayout subNav;
    private ImageView btnBack;

    private String sId;
    private boolean[] ckbs;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Intent intent = getIntent();
        sId = intent.getStringExtra("student");

        myDbManager = new DatabaseManager(ExamActivity.this);
        tvTitle = findViewById(R.id.tvExamTitle);
        lvExams = findViewById(R.id.lvExams);
        btnAddExam = findViewById(R.id.imgAddExam);
        tblExamDetail = findViewById(R.id.tblExamDetail);
        etUnitName = findViewById(R.id.etUnitName);
        etEDate = findViewById(R.id.etEDate);
        etETime = findViewById(R.id.etETime);
        etLocation = findViewById(R.id.etELocation);
        btnAddNewExam = findViewById(R.id.btnAddNewExam);
        btnBackToExamList = findViewById(R.id.btnBacktoExamList);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnDeleteExam = findViewById(R.id.imgDeleteExam);
        subNav = findViewById(R.id.subNav);
        btnBack = findViewById(R.id.imgBack);

        getList(sId);
        getCurrentTime();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExamActivity.this, StudentDetailsActivity.class);
                intent.putExtra("student", sId);
                startActivity(intent);
            }
        });

        btnAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInputs();
                tblExamDetail.setVisibility(View.VISIBLE);
                lvExams.setVisibility(View.GONE);
                tvTitle.setText("New Exam:");
                btnAddExam.setVisibility(View.GONE);
                btnDeleteExam.setVisibility(View.GONE);
                subNav.setVisibility(View.GONE);
            }
        });

        btnAddNewExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String unitN = String.valueOf(etUnitName.getText());
                String date = String.valueOf(etEDate.getText());
                String time = String.valueOf(etETime.getText());

                if(unitN.length() != 0 && date.length()!= 0 && time.length()!=0){
                    Exam exam = new Exam();
                    exam.setStudentId(String.valueOf(sId));
                    exam.setUnitName(String.valueOf(etUnitName.getText()));
                    exam.seteDate(String.valueOf(etEDate.getText()));
                    exam.seteTime(String.valueOf(etETime.getText()));
                    exam.seteLocation(String.valueOf(etLocation.getText()));

                    myDbManager.addExam(exam);
                    getList(sId);

                } else {
                    if(unitN.length() == 0){
                        Toast.makeText(ExamActivity.this,
                                "Unit name cannot be empty!",
                                Toast.LENGTH_LONG).show();
                    }

                    if(etEDate.length() == 0){
                        Toast.makeText(ExamActivity.this,
                                "Exam date cannot be empty!",
                                Toast.LENGTH_LONG).show();
                    }

                    if(etETime.length() == 0){
                        Toast.makeText(ExamActivity.this,
                                "Exam time cannot be empty!",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnBackToExamList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getList(sId);
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dpDialog = new DatePickerDialog(
                        ExamActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        etEDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);

                dpDialog.setTitle("Exam Date");
                dpDialog.show();

            }
        });

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog tpDialog = new TimePickerDialog(
                        ExamActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        etETime.setText(String.format("%02d:%02d",hourOfDay, minute));
                        mHour = hourOfDay;
                        mMinute = minute;
                    }
                }, mHour, mMinute, true);

                tpDialog.setTitle("Exam Time");
                tpDialog.show();
            }
        });

        btnDeleteExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int[] eIds = new int[examList.size()];
                ckbs = arrayAdpt.getCheckBoxState();
                for(int i = 0; i < examList.size(); i++){
                    if(ckbs[i] == true){
                        for (int j = 0; j < examList.size(); j++){
                            if (eIds[j] == 0){
                                eIds[j] = arrayAdpt.getExamId(i);
                                break;
                            }
                        }

                    }
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExamActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Selected Exams will be deleted permanently." +
                        "\nAre you sure you want to delete these records?");
                alertDialog.setCancelable(true);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int eId: eIds) {
                            myDbManager.deleteExamRecord(eId);
                        }
                        getList(sId);
                    }
                });
                alertDialog.show();

            }
        });
    }

    public void getList(String studentId){
        btnAddExam.setVisibility(View.VISIBLE);
        btnDeleteExam.setVisibility(View.VISIBLE);
        lvExams.setVisibility(View.VISIBLE);
        subNav.setVisibility(View.VISIBLE);
        tblExamDetail.setVisibility(View.GONE);

        myDbManager.openReadable();
        examList = myDbManager.getStudentExams(studentId);
        arrayAdpt = new ExamAdapter(this, examList);
        lvExams.setAdapter(arrayAdpt);
        tvTitle.setText(sId + "'s Exams (" + examList.size() +")");

        clearInputs();
    }

    public void clearInputs(){
        etUnitName.setText("");
        etLocation.setText("");
        etEDate.setText("");
        etETime.setText("");
        getCurrentTime();
    }

    public void getCurrentTime(){
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);
        mHour = calender.get(Calendar.HOUR_OF_DAY);
        mMinute = calender.get(Calendar.MINUTE);
    }

}
