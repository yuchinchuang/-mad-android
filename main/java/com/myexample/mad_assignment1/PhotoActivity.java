package com.myexample.mad_assignment1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    private DatabaseManager myDbManager;
    private ImageView img;
    private Button btnAssign;
    private ImageView btnBack;
    private ImageView btnDelete;
    private TextView tvTitle;
    private int pId;
    private GalleryPhoto photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        myDbManager = new DatabaseManager(PhotoActivity.this);
        img = findViewById(R.id.imgFullScreen);
        btnAssign = findViewById(R.id.btnAssignPhoto);
        btnBack = findViewById(R.id.imgBackGallery);
        btnDelete = findViewById(R.id.imgDeletePhoto);
        tvTitle = findViewById(R.id.tvTitle_photo);

        Intent intent = getIntent();
        pId = intent.getIntExtra("photo", -1);

        if(pId != -1){
            photo = myDbManager.getPhoto(pId);
            File sPhoto = new File(photo.getPhotoPath());
            if(sPhoto.exists()){
                Bitmap photoBitmap = BitmapFactory.decodeFile(sPhoto.getAbsolutePath());
                img.setImageBitmap(photoBitmap);
            }
            tvTitle.setText("Photo");
        } else {
            tvTitle.setText("Photo not found");
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhotoActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Selected photos will be deleted from this application." +
                        "\nAre you sure you want to delete these photos?");
                alertDialog.setCancelable(true);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDbManager.deletePhoto(pId);
                        Intent intent = new Intent(PhotoActivity.this, MainActivity.class);
                        intent.putExtra("fragment", "gallery");
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoActivity.this, MainActivity.class);
                intent.putExtra("fragment", "gallery");
                startActivity(intent);
            }
        });
    }
}
