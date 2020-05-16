package com.myexample.mad_assignment1;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExamAdapter extends ArrayAdapter<Exam> {

    private Context context;
    private ArrayList<Exam> exams;
    private boolean[] ckbState;

    public ExamAdapter(@NonNull Context context, ArrayList<Exam> exams) {
        super(context, R.layout.rowlayout_exam, exams);

        this.context = context;
        this.exams = exams;
        ckbState = new boolean[exams.size()];
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout_exam, parent, false);

        final CheckBox ckbExam = rowView.findViewById(R.id.ckbSelectExam);
        TextView tvUnitName = rowView.findViewById(R.id.tvUnitName);
        TextView tvDate = rowView.findViewById(R.id.tvEDate);
        TextView tvTime = rowView.findViewById(R.id.tvETime);
        TextView tvLocation = rowView.findViewById(R.id.tvELocation);
        ImageView imgFlag = rowView.findViewById(R.id.imgFlag);

        Exam exam = exams.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar calender = Calendar.getInstance();
        Date currentDateTime = calender.getTime();
        Date examDateTime = new Date();
        String strDateTime = exam.geteDate() + " " + exam.geteTime();
        try {
            examDateTime = dateFormat.parse(strDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(examDateTime.before(currentDateTime)){
            imgFlag.setImageResource(R.drawable.past);
        }

        if (examDateTime.after(currentDateTime)){
            imgFlag.setImageResource(R.drawable.current);
        }

        tvUnitName.setText(exam.getUnitName());
        tvDate.setText(exam.geteDate());
        tvTime.setText(exam.geteTime());
        tvLocation.setText(exam.geteLocation());

        ckbExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    ckbState[position] = true;
                } else {
                    ckbState[position] = false;
                }
            }
        });

        return rowView;
    }

    public boolean[] getCheckBoxState(){
        return ckbState;
    }

    public int getExamId(int position){
        Exam exam = exams.get(position);
        return exam.getExamId();
    }
}
