package com.myexample.mad_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(new HomeFragment());
                    return true;
                case R.id.navigation_student:
                    setFragment(new StudentActivity());
                    return true;
                case R.id.navigation_task:
                    setFragment(new TaskActivity());
                    return true;
                case R.id.navigation_gallery:
                    setFragment(new GalleryActivity());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        intent = getIntent();

        String fragment = intent.getStringExtra("fragment");
        if(fragment != null){
            switch (fragment) {
                case "Home":
                    setFragment(new HomeFragment());
                    navigation.setSelectedItemId(R.id.navigation_home);
                    break;
                case "student":
                    setFragment(new StudentActivity());
                    navigation.setSelectedItemId(R.id.navigation_student);
                    break;
                case "task":
                    setFragment(new TaskActivity());
                    navigation.setSelectedItemId(R.id.navigation_task);
                    break;
                case "gallery":
                    setFragment(new GalleryActivity());
                    navigation.setSelectedItemId(R.id.navigation_gallery);
                    break;
            }
        } else {
            setFragment(new HomeFragment());
            navigation.setSelectedItemId(R.id.navigation_home);
        }

    }

    protected void setFragment(Fragment f){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, f);
        ft.commit();
    }

}
