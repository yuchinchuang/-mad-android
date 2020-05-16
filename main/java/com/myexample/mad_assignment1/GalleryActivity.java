package com.myexample.mad_assignment1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GalleryActivity extends Fragment {

    private DatabaseManager myDbManager;
    private TextView tvTitle;
    private GridView gridView;
    private ArrayList<GalleryPhoto> photos;
    private GalleryAdapter adapter;
    private ImageView btnAddPhoto;

    private boolean[] ckbs;
    private boolean isSelected;

    private Bitmap bitmap;
    private String imgPath;
    private File destination;
    private GalleryPhoto photo;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_IMG = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDbManager = new DatabaseManager(getActivity());
        tvTitle = view.findViewById(R.id.tvGalleryTitle);
        gridView = view.findViewById(R.id.gridGallery);
        btnAddPhoto = view.findViewById(R.id.imgAddPhoto);

        isStoragePermissionGranted();
        getPhotoList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GalleryPhoto p = (GalleryPhoto) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), PhotoActivity.class);
                intent.putExtra("photo", p.getPhotoId());
                startActivity(intent);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] phptoOption = new CharSequence[]{"Camera", "Gallery"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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

    public void getPhotoList(){
        photos = myDbManager.getAllGalleryPhoto();
        adapter = new GalleryAdapter(getContext(), photos);
        gridView.setAdapter(adapter);
        tvTitle.setText("Photo Gallery (" + photos.size() + ")");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photo = new GalleryPhoto();
        if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
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
                photo.setPhotoPath(imgPath);
                myDbManager.addPhoto(photo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PICK_IMG) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                photo.setPhotoPath(imgPath);
                myDbManager.addPhoto(photo);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("photo exception", e.getMessage());
            }
        }

        getPhotoList();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String TAG;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public boolean isStoragePermissionGranted(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.v( TAG ,"Permission is granted");
            return true;
        } else {
            Log.v(TAG, "Permission is revoked");
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
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
}
