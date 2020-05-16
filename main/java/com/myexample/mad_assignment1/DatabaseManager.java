package com.myexample.mad_assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static final String DB_NAME = "MAD_Assignment1";
    public static final int DB_VERSION = 7;

    public static final String DB_TABLE_STUDENT = "StudentInfo";
    public static final String COLUMN_S_ID = "studentId";
    public static final String COLUMN_S_FNAME = "sFirstName";
    public static final String COLUMN_S_LNAME = "sLastName";
    public static final String COLUMN_S_COURSE = "courseStudy";
    public static final String COLUMN_S_GENDER = "gender";
    public static final String COLUMN_S_AGE = "sAge";
    public static final String COLUMN_S_ADDRESS = "address";

    public static final String DB_TABLE_TASK = "ToDoTask";
    public static final String COLUMN_T_ID = "taskId";
    public static final String COLUMN_T_NAME = "taskName";
    public static final String COLUMN_T_LOCATION = "tLocation";
    public static final String COLUMN_T_ISCOMPLETED = "isCompleted";

    public static final String DB_TABLE_EXAM = "Exam";
    public static final String COLUMN_E_ID = "examId";
    public static final String COLUMN_E_NAME = "unitName";
    public static final String COLUMN_E_DATE = "eDate";
    public static final String COLUMN_E_TIME = "eTime";
    public static final String COLUMN_E_LOCATION = "eLocation";
    public static final String COLUMN_E_SID = "studentId";

    public static final String DB_TABLE_PHOTO = "Photo";
    public static final String COLUMN_P_ID = "photoId";
    public static final String COLUMN_P_PATH = "photoPath";
    public static final String COLUMN_P_SID = "studentId";

    // Create table string
    private static final String CREATE_TABLE_STUDENT = "CREATE TABLE " + DB_TABLE_STUDENT +
            " ( " + COLUMN_S_ID + " TEXT, " +
            COLUMN_S_FNAME + " TEXT, " +
            COLUMN_S_LNAME + " TEXT, " +
            COLUMN_S_COURSE + " TEXT, " +
            COLUMN_S_GENDER + " TEXT, " +
            COLUMN_S_AGE + " INTEGER, " +
            COLUMN_S_ADDRESS + " TEXT );";
    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + DB_TABLE_TASK +
            " ( " + COLUMN_T_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_T_NAME + " TEXT, " +
            COLUMN_T_LOCATION + " TEXT, " +
            COLUMN_T_ISCOMPLETED + " INTEGER);";

    private static final String CREATE_TABLE_EXAM = "CREATE TABLE " + DB_TABLE_EXAM +
            " ( " + COLUMN_E_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_E_NAME + " TEXT, " +
            COLUMN_E_DATE + " TEXT, " +
            COLUMN_E_TIME + " TEXT, " +
            COLUMN_E_LOCATION + " TEXT, " +
            COLUMN_E_SID + " TEXT);";

    private static final String CREATE_TABLE_PHOTO = "CREATE TABLE " + DB_TABLE_PHOTO +
            " ( " + COLUMN_P_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_P_PATH + " TEXT, " +
            COLUMN_P_SID + " TEXT);";

    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    // Database
    public DatabaseManager(Context c){
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getReadableDatabase();
    }

    public DatabaseManager openReadable() throws android.database.SQLException{
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    public void close(){
        helper.close();
    }

    //Insert
    public boolean addStudentRecord(Student student){
        synchronized (this.db){
            ContentValues newStudent = new ContentValues();
            newStudent.put(COLUMN_S_ID, student.getStudentId());
            newStudent.put(COLUMN_S_FNAME, student.getsFirstName());
            newStudent.put(COLUMN_S_LNAME, student.getsLastName());
            newStudent.put(COLUMN_S_COURSE, student.getCourseStudy());
            newStudent.put(COLUMN_S_GENDER, student.getGender());
            newStudent.put(COLUMN_S_AGE, student.getsAge());
            newStudent.put(COLUMN_S_ADDRESS, student.getAddress());

            try {
                db.insertOrThrow(DB_TABLE_STUDENT, null, newStudent);
            } catch (Exception e){
                Log.e("Error in inserting students: ", e.toString());
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    public boolean addTask(ToDoTask task){
        synchronized (this.db){
            ContentValues newTask = new ContentValues();

            newTask.put(COLUMN_T_NAME, task.getTaskName());
            newTask.put(COLUMN_T_LOCATION, task.gettLocation());
            newTask.put(COLUMN_T_ISCOMPLETED, 0);

            try {
                long newRowId = db.insertOrThrow(DB_TABLE_TASK, null, newTask);
                ToDoTask theTask = new ToDoTask();
                theTask.setTaskId((int)newRowId);
                theTask.setTaskName(task.getTaskName());
                theTask.settLocation(task.gettLocation());
                theTask.setCompleted(0);
                updateTask(theTask);
            } catch (Exception e){
                Log.e("Error in inserting task: ", e.toString());
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    public boolean addExam(Exam exam){
        synchronized (this.db){
            ContentValues newExam = new ContentValues();

            newExam.put(COLUMN_E_SID, exam.getStudentId());
            newExam.put(COLUMN_E_NAME, exam.getUnitName());
            newExam.put(COLUMN_E_DATE, exam.geteDate());
            newExam.put(COLUMN_E_TIME, exam.geteTime());
            newExam.put(COLUMN_E_LOCATION, exam.geteLocation());

            try {
                long newRowId = db.insertOrThrow(DB_TABLE_EXAM, null, newExam);
                Exam theExam = new Exam();
                theExam.setExamId((int)newRowId);
                theExam.setStudentId(exam.getStudentId());
                theExam.setUnitName(exam.getUnitName());
                theExam.seteDate(exam.geteDate());
                theExam.seteTime(exam.geteTime());
                theExam.seteLocation(exam.geteLocation());
                updateExam(theExam);
            } catch (Exception e){
                Log.e("Error in inserting exam: ", e.toString());
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    public boolean addPhoto(GalleryPhoto photo){
        synchronized (this.db){
            ContentValues newPhoto = new ContentValues();

            newPhoto.put(COLUMN_P_PATH, photo.getPhotoPath());
            newPhoto.put(COLUMN_P_SID, photo.getStudentId());

            try {
                long newRowId = db.insertOrThrow(DB_TABLE_PHOTO, null, newPhoto);
                GalleryPhoto thePhoto = new GalleryPhoto();
                thePhoto.setPhotoId((int)newRowId);
                thePhoto.setPhotoPath(photo.getPhotoPath());
                thePhoto.setStudentId(photo.getStudentId());
                updatePhoto(thePhoto);
            } catch (Exception e){
                Log.e("Error in inserting exam: ", e.toString());
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    // Select
    public Student getStudentRecord(String s_id){
        String selectQuery = "SELECT * FROM " + DB_TABLE_STUDENT +
                " WHERE " + COLUMN_S_ID + " = '" + s_id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        Student s = new Student();
        s.setStudentId(cursor.getString(cursor.getColumnIndex(COLUMN_S_ID)));
        s.setsFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_S_FNAME)));
        s.setsLastName(cursor.getString(cursor.getColumnIndex(COLUMN_S_LNAME)));
        s.setCourseStudy(cursor.getString(cursor.getColumnIndex(COLUMN_S_COURSE)));
        s.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_S_GENDER)));
        s.setsAge(cursor.getInt(cursor.getColumnIndex(COLUMN_S_AGE)));
        s.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_S_ADDRESS)));

        return s;

    }

    public ToDoTask getTaskRecord(int taskId){
        String selectQuery = "SELECT * FROM " + DB_TABLE_TASK +
                " WHERE " + COLUMN_T_ID + " = " + taskId + "";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        ToDoTask t = new ToDoTask();
        t.setTaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_T_ID)));
        t.setTaskName(cursor.getString(cursor.getColumnIndex(COLUMN_T_NAME)));
        t.settLocation(cursor.getString(cursor.getColumnIndex(COLUMN_T_LOCATION)));
        t.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_T_ISCOMPLETED)));

        return t;

    }

    public GalleryPhoto getPhoto(int photoId){
        String selectQuery = "SELECT * FROM " + DB_TABLE_PHOTO +
                " WHERE " + COLUMN_P_ID + " = " + photoId + "";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        GalleryPhoto p = new GalleryPhoto();
        p.setPhotoId(cursor.getInt(cursor.getColumnIndex(COLUMN_P_ID)));
        p.setPhotoPath(cursor.getString(cursor.getColumnIndex(COLUMN_P_PATH)));
        p.setStudentId(cursor.getString(cursor.getColumnIndex(COLUMN_P_SID)));

        return p;

    }

    public ArrayList<Student> getAllStudentRecords(){
        ArrayList<Student> students = new ArrayList<Student>();
        String selectQuery = "SELECT * FROM " + DB_TABLE_STUDENT;

        Cursor cursor  = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            do{
                Student s = new Student();
                s.setStudentId(cursor.getString(cursor.getColumnIndex(COLUMN_S_ID)));
                s.setsFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_S_FNAME)));
                s.setsLastName(cursor.getString(cursor.getColumnIndex(COLUMN_S_LNAME)));
                s.setCourseStudy(cursor.getString(cursor.getColumnIndex(COLUMN_S_COURSE)));
                s.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_S_GENDER)));
                s.setsAge(cursor.getInt(cursor.getColumnIndex(COLUMN_S_AGE)));
                s.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_S_ADDRESS)));

                students.add(s);
            } while (cursor.moveToNext());
        }

        return students;
    }

    public ArrayList<ToDoTask> getAllTasks(){
        ArrayList<ToDoTask> tasks = new ArrayList<ToDoTask>();
        String selectQuery = "SELECT * FROM " + DB_TABLE_TASK;

        Cursor cursor  = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            do{
                ToDoTask t = new ToDoTask();
                t.setTaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_T_ID)));
                t.setTaskName(cursor.getString(cursor.getColumnIndex(COLUMN_T_NAME)));
                t.settLocation(cursor.getString(cursor.getColumnIndex(COLUMN_T_LOCATION)));
                t.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_T_ISCOMPLETED)));

                tasks.add(t);
            } while (cursor.moveToNext());
        }

        return tasks;
    }

    public ArrayList<Exam> getStudentExams(String studentId){
        ArrayList<Exam> exams = new ArrayList<Exam>();
        String selectQuery = "SELECT * FROM " + DB_TABLE_EXAM + " WHERE " + COLUMN_E_SID + "='" + studentId + "'";

        Cursor cursor  = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            do{
                Exam e = new Exam();
                e.setExamId(cursor.getInt(cursor.getColumnIndex(COLUMN_E_ID)));
                e.setUnitName(cursor.getString(cursor.getColumnIndex(COLUMN_E_NAME)));
                e.seteDate(cursor.getString(cursor.getColumnIndex(COLUMN_E_DATE)));
                e.seteTime(cursor.getString(cursor.getColumnIndex(COLUMN_E_TIME)));
                e.seteLocation(cursor.getString(cursor.getColumnIndex(COLUMN_E_LOCATION)));
                e.setStudentId(cursor.getString(cursor.getColumnIndex(COLUMN_E_SID)));

                exams.add(e);
            } while (cursor.moveToNext());
        }

        return exams;
    }

    public ArrayList<GalleryPhoto> getAllGalleryPhoto(){
        ArrayList<GalleryPhoto> photos = new ArrayList<GalleryPhoto>();
        String selectQuery = "SELECT * FROM " + DB_TABLE_PHOTO;

        Cursor cursor  = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            do{
                GalleryPhoto p = new GalleryPhoto();
                p.setPhotoId(cursor.getInt(cursor.getColumnIndex(COLUMN_P_ID)));
                p.setPhotoPath(cursor.getString(cursor.getColumnIndex(COLUMN_P_PATH)));
                p.setStudentId(cursor.getString(cursor.getColumnIndex(COLUMN_P_SID)));

                photos.add(p);
            } while (cursor.moveToNext());
        }

        return photos;
    }

    //Update
    public int updateStudentRecords(Student student){

        ContentValues aStudent = new ContentValues();
        aStudent.put(COLUMN_S_FNAME, student.getsFirstName());
        aStudent.put(COLUMN_S_LNAME, student.getsLastName());
        aStudent.put(COLUMN_S_COURSE, student.getCourseStudy());
        aStudent.put(COLUMN_S_GENDER, student.getGender());
        aStudent.put(COLUMN_S_AGE, student.getsAge());
        aStudent.put(COLUMN_S_ADDRESS, student.getAddress());

        return db.update(DB_TABLE_STUDENT, aStudent,
                COLUMN_S_ID + " = '" + student.getStudentId() + "'",
                null);
    }

    public int updateTask(ToDoTask task){

        ContentValues aTask = new ContentValues();
        aTask.put(COLUMN_T_ID, task.getTaskId());
        aTask.put(COLUMN_T_NAME, task.getTaskName());
        aTask.put(COLUMN_T_LOCATION, task.gettLocation());
        aTask.put(COLUMN_T_ISCOMPLETED, task.getIsCompleted());

        return db.update(DB_TABLE_TASK, aTask,
                COLUMN_T_ID + " = '" + task.getTaskId() + "'",
                null);
    }

    public int updateExam(Exam exam){

        ContentValues aExam = new ContentValues();
        aExam.put(COLUMN_E_ID, exam.getExamId());
        aExam.put(COLUMN_E_SID, exam.getStudentId());
        aExam.put(COLUMN_E_NAME, exam.getUnitName());
        aExam.put(COLUMN_E_DATE, String.valueOf(exam.geteDate()));
        aExam.put(COLUMN_E_TIME, String.valueOf(exam.geteTime()));
        aExam.put(COLUMN_E_LOCATION, exam.geteLocation());

        return db.update(DB_TABLE_EXAM, aExam,
                COLUMN_E_ID + " = '" + exam.getExamId() + "'",
                null);
    }

    public int updatePhoto(GalleryPhoto photo){

        ContentValues aPboto = new ContentValues();
        aPboto.put(COLUMN_P_ID, photo.getPhotoId());
        aPboto.put(COLUMN_P_PATH, photo.getPhotoPath());
        aPboto.put(COLUMN_P_SID, photo.getStudentId());

        return db.update(DB_TABLE_PHOTO, aPboto,
                COLUMN_P_ID + " = '" + photo.getPhotoId() + "'",
                null);
    }

    //Delete
    public void deleteStudentRecord(String studentId){
        db.delete(DB_TABLE_STUDENT,
                COLUMN_S_ID + " = '" + studentId + "'",
                null);
    }

    public void deleteExamRecord(int examId){
        db.delete(DB_TABLE_EXAM,
                COLUMN_E_ID + " = '" + examId + "'",
                null);
    }

    public void deletePhoto(int photoId){
        db.delete(DB_TABLE_PHOTO,
                COLUMN_P_ID + " = '" + photoId + "'",
                null);
    }

    // SQLite Open Helper
    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper(Context c){
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STUDENT);
            db.execSQL(CREATE_TABLE_TASK);
            db.execSQL(CREATE_TABLE_EXAM);
            db.execSQL(CREATE_TABLE_PHOTO);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Student table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STUDENT);

            Log.w("Task table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_TASK);

            Log.w("Exam table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_EXAM);

            Log.w("Photo table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_PHOTO);

            onCreate(db);
        }
    }
}
