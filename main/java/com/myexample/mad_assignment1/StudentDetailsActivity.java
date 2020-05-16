package com.myexample.mad_assignment1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentDetailsActivity extends AppCompatActivity {

    LinearLayout subNav;
    private DatabaseManager myDbManager;
    private TextView tvTitle;
    private Button btnAddNewStudent;
    private ImageView btnBack;
    private ImageView btnEditStudent;
    private ImageView btnDeleteStudent;

    private Student student;
    private EditText etSId;
    private EditText etFN;
    private EditText etLN;
    private EditText etCourse;
    private Spinner spGender;
    private NumberPicker npAge;
    private EditText etAddress;
    private Button btnCancel;
    private Button btnUpdate;
    private TableRow tblRowBtns;
    private ImageView imgStudentPhoto;
    private Button btnChoosePhoto;
    private ImageView btnDisplayMap;
    private Button btnStudentExam;

    private String sId = "";
    private String gender;
    private int age;
    private Bitmap bitmap;
    private String imgPath;
    private File destination;
    private ArrayList<GalleryPhoto> photos;
    private GalleryPhoto photo;
    private boolean hasPhoto;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_IMG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        subNav = findViewById(R.id.subNav);
        myDbManager = new DatabaseManager(StudentDetailsActivity.this);
        tvTitle = findViewById(R.id.tvTitle_student);
        btnAddNewStudent = findViewById(R.id.btnInsertStudentData);
        btnBack = findViewById(R.id.imgBack);
        btnEditStudent = findViewById(R.id.imgEditStudent);
        btnDeleteStudent = findViewById(R.id.imgDeleteStudent);
        imgStudentPhoto = findViewById(R.id.imgStudent);
        btnChoosePhoto = findViewById(R.id.btnChooseStudentImg);
        etSId = findViewById(R.id.etSId);
        etFN = findViewById(R.id.etSFN);
        etLN = findViewById(R.id.etSLN);
        etCourse = findViewById(R.id.etSCourse);
        spGender = findViewById(R.id.spGender);
        npAge = findViewById(R.id.npAge);
        etAddress = findViewById(R.id.etSAddress);
        tblRowBtns = findViewById(R.id.tblRow_btns);
        btnCancel = findViewById(R.id.btnCancelEditStudent);
        btnUpdate = findViewById(R.id.btnUpdateStudentData);
        btnDisplayMap = findViewById(R.id.imgDisplayMap);
        btnStudentExam = findViewById(R.id.btnStudentExams);

        Intent intent = getIntent();
        sId = intent.getStringExtra("student");
        photo = new GalleryPhoto();

        if (sId != null){
            DisableInputs();
            GetStudentDetails(sId);
            age = student.getsAge();
        } else {
            age = 0;
            EnableInputs();
            btnAddNewStudent.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            imgStudentPhoto.setVisibility(View.GONE);
            tvTitle.setText("New Student");
        }

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.argb(255,255,255,255));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SetNpRange();
        npAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                age = npAge.getValue();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity(MainActivity.class, "fragment", "student");
            }
        });

        btnAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Student> studentList = myDbManager.getAllStudentRecords();
                Student student = new Student();
                GalleryPhoto studentPhoto = new GalleryPhoto();
                String sId = String.valueOf(etSId.getText());
                boolean isExist = false;
                for (int i = 0; i < studentList.size(); i++){
                    if(studentList.get(i).getStudentId().equals(sId)){
                        isExist = true;
                        Toast.makeText(
                                StudentDetailsActivity.this,
                                "Student Id: " + sId + " is existing in system, please use another one.",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                if(!isExist) {
                    student.setStudentId(String.valueOf(etSId.getText()));
                    student.setsFirstName(String.valueOf(etFN.getText()));
                    student.setsLastName(String.valueOf(etLN.getText()));
                    student.setCourseStudy(String.valueOf(etCourse.getText()));
                    student.setGender(String.valueOf(spGender.getSelectedItem()));
                    student.setsAge(age);
                    student.setAddress(String.valueOf(etAddress.getText()));

                    if(photo.getPhotoPath() != null){
                        studentPhoto.setStudentId(sId);
                        studentPhoto.setPhotoPath(photo.getPhotoPath());
                        myDbManager.addPhoto(studentPhoto);
                    }
                    myDbManager.addStudentRecord(student);
                    ChangeActivity(MainActivity.class, "fragment", "student");
                }

            }
        });

        btnEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnableInputs();
                age = student.getsAge();
                btnUpdate.setVisibility(View.VISIBLE);
                btnAddNewStudent.setVisibility(View.GONE);
                tvTitle.setText("Edit Student Record");
            }
        });

        btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentDetailsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alertDialog.setTitle("Delete Student Record");
                alertDialog.setMessage("Student's record will be deleted permanently." +
                        "\nAre you sure you want to delete this record?");
                alertDialog.setCancelable(true);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UpdateViewData();
                    }
                });
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDbManager.deleteStudentRecord(sId);
                        ChangeActivity(MainActivity.class, "fragment", "student");
                    }
                });
                alertDialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sId != null){
                    UpdateViewData();
                } else {
                    ChangeActivity(MainActivity.class, "fragment", "student");
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student s = new Student();
                s.setStudentId(String.valueOf(etSId.getText()));
                s.setsFirstName(String.valueOf(etFN.getText()));
                s.setsLastName(String.valueOf(etLN.getText()));
                s.setCourseStudy(String.valueOf(etCourse.getText()));
                s.setGender(String.valueOf(spGender.getSelectedItem()));
                s.setsAge(age);
                s.setAddress(String.valueOf(etAddress.getText()));
                myDbManager.updateStudentRecords(s);

                if(photo.getPhotoPath() != null){
                    if(hasPhoto){
                        myDbManager.updatePhoto(photo);
                    } else {
                        myDbManager.addPhoto(photo);
                    }
                }
                UpdateViewData();
            }
        });

        btnDisplayMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = String.valueOf(etAddress.getText());
                ChangeActivity(MapsActivity.class, "address", address);
            }
        });

        btnStudentExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity(ExamActivity.class, "student", sId);
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] phptoOption = new CharSequence[]{"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetailsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setTitle("Select photo from:");
                builder.setItems(phptoOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent pickPhoto = null;
                        switch (i){
                            case 0:
                                pickPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(pickPhoto, REQUEST_TAKE_PHOTO);
                                break;
                            case 1:
                                pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, REQUEST_PICK_IMG);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });


    }

    public void GetStudentDetails(String studentId){
        myDbManager.openReadable();
        student = myDbManager.getStudentRecord(studentId);

        etSId.setText(student.getStudentId());
        etFN.setText(student.getsFirstName());
        etLN.setText(student.getsLastName());
        etCourse.setText(student.getCourseStudy());

        gender = student.getGender();
        for(int i = 0; i < spGender.getCount(); i++){
            if (spGender.getItemAtPosition(i).toString().equalsIgnoreCase(gender)){
                spGender.setSelection(i);
                break;
            }
        }

        SetNpRange();
        npAge.setValue(student.getsAge());

        etAddress.setText(student.getAddress());

        photos = myDbManager.getAllGalleryPhoto();
        hasPhoto = false;

        for (GalleryPhoto p: photos) {
            if(p.getStudentId() != null){
                if(p.getStudentId().equals(sId)){
                    photo.setPhotoId(p.getPhotoId());
                    photo.setPhotoPath(p.getPhotoPath());
                    photo.setStudentId(p.getStudentId());
                    hasPhoto = true;
                }
            }
        }

        isStoragePermissionGranted();
        if(hasPhoto){
            File sPhoto = new File(photo.getPhotoPath());
            if(sPhoto.exists()){
                Bitmap photoBitmap = BitmapFactory.decodeFile(sPhoto.getAbsolutePath());
                imgStudentPhoto.setImageBitmap(photoBitmap);
            }
        } else {
            imgStudentPhoto.setImageResource(R.drawable.photo);
        }
    }

    public void SetNpRange(){
        npAge.setMinValue(0);
        npAge.setMaxValue(100);
    }
    public void EnableInputs(){
        etFN.setEnabled(true);
        etLN.setEnabled(true);
        etCourse.setEnabled(true);
        spGender.setEnabled(true);
        npAge.setEnabled(true);
        etAddress.setEnabled(true);
        tblRowBtns.setVisibility(View.VISIBLE);
        btnChoosePhoto.setVisibility(View.VISIBLE);
        btnEditStudent.setVisibility(View.GONE);
        btnDeleteStudent.setVisibility(View.GONE);
        btnDisplayMap.setVisibility(View.GONE);
        subNav.setVisibility(View.GONE);
    }

    public void DisableInputs(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etSId.setEnabled(false);
        etFN.setEnabled(false);
        etLN.setEnabled(false);
        etCourse.setEnabled(false);
        spGender.setEnabled(false);
        npAge.setEnabled(false);
        etAddress.setEnabled(false);
        tblRowBtns.setVisibility(View.GONE);
        btnChoosePhoto.setVisibility(View.GONE);
        tvTitle.setText("Student Details");
        subNav.setVisibility(View.VISIBLE);
    }

    public void UpdateViewData(){
        GetStudentDetails(sId);
        DisableInputs();
        btnEditStudent.setVisibility(View.VISIBLE);
        btnDeleteStudent.setVisibility(View.VISIBLE);
        btnDisplayMap.setVisibility(View.VISIBLE);
    }

    public void ChangeActivity(Class c, String extraName, String extraStr){
        Intent intent = new Intent(StudentDetailsActivity.this, c);
        if(extraName != null ){
            intent.putExtra(extraName, extraStr);
        }
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(storageDir, "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imgStudentPhoto.setImageBitmap(bitmap);

                setPhoto(imgPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PICK_IMG) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                        Log.e("Activity", "Pick from Gallery::>>> ");

                        imgPath = getRealPathFromURI(selectedImage);
                        destination = new File(imgPath.toString());
                        imgStudentPhoto.setImageBitmap(bitmap);

                        setPhoto(imgPath);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("photo exception", e.getMessage());
                    }
                }
            } else {
                Toast.makeText(this, "No photo is selected.", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void setPhoto(String imgPath){
        photo.setPhotoPath(imgPath);
        photo.setStudentId(sId);
        if(photo.getPhotoPath() != null){
            imgStudentPhoto.setVisibility(View.VISIBLE);
        } else {
            imgStudentPhoto.setVisibility(View.GONE);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // external storage permission - start
    private String TAG;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public boolean isStoragePermissionGranted(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.v( TAG ,"Permission is granted");
            return true;
        } else {
            Log.v(TAG, "Permission is revoked");
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG, "Permission: " + permissions[0]+ " was" + grantResults);
        }
    }
    // external storage permission - end
}
