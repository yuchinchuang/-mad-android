package com.myexample.mad_assignment1;

public class Student {
    private String studentId;
    private String sFirstName;
    private String sLastName;
    private String courseStudy;
    private String gender;
    private int sAge;
    private String address;


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getsFirstName() {
        return sFirstName;
    }

    public void setsFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getsLastName() {
        return sLastName;
    }

    public void setsLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getCourseStudy() {
        return courseStudy;
    }

    public void setCourseStudy(String courseStudy) {
        this.courseStudy = courseStudy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getsAge() {
        return sAge;
    }

    public void setsAge(int sAge) {
        this.sAge = sAge;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
