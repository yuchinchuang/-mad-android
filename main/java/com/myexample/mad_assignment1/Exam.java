package com.myexample.mad_assignment1;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Exam {
    private int examId;
    private String unitName;
    private String eDate;
    private String eTime;
    private String eLocation;
    private String studentId;

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String geteLocation() {
        return eLocation;
    }

    public void seteLocation(String eLocation) {
        this.eLocation = eLocation;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
