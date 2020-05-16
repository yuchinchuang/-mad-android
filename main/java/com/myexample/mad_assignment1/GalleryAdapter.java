package com.myexample.mad_assignment1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private ArrayList<GalleryPhoto> photos;
    private Context context;
    private boolean[] ckbState;

    public GalleryAdapter(Context context, ArrayList<GalleryPhoto> photos){

        this.context = context;
        this.photos = photos;
        ckbState = new boolean[photos.size()];
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View gridView= layoutInflater.inflate(R.layout.gridlayout_gallery, parent, false);
        ImageView img = gridView.findViewById(R.id.imgGridPhoto);
        final CheckBox ckb = gridView.findViewById(R.id.ckbSelectPhoto);
        //ckb.setVisibility(View.GONE);

        GalleryPhoto photo = photos.get(position);
        File sPhoto = new File(photo.getPhotoPath());
        if(sPhoto.exists()){
            Bitmap photoBitmap = BitmapFactory.decodeFile(sPhoto.getAbsolutePath());
            img.setImageBitmap(photoBitmap);
        }

        ckb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    ckbState[position] = true;
                } else {
                    ckbState[position] = false;
                }
            }
        });

        return gridView;
    }

    public boolean[] getCheckBoxState(){
        return ckbState;
    }

    public int getPhotoId(int position){
        GalleryPhoto photo = photos.get(position);
        return photo.getPhotoId();
    }

}
