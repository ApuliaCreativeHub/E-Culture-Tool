package com.apuliacreativehub.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.fragments.PathsFragment;
import com.apuliacreativehub.eculturetool.fragments.PlacesFragment;
import com.apuliacreativehub.eculturetool.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        loadDefaultFragment();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment activeFragment = mapperFragment(item.getItemId());
                loadFragment(activeFragment);
                return true;
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_frame_layout, fragment);
            ft.commit();
            return true;
        }
        return false;
    }

    private boolean loadDefaultFragment() {
        bottomNavigationView.setSelectedItemId(R.id.menuItemUser);
        return loadFragment(new UserFragment());
    }

    private Fragment mapperFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.menuItemPlaces:
                fragment = new PlacesFragment();
                break;
            case R.id.menuItemUser:
                fragment = new UserFragment();
                break;
            case R.id.menuItemPaths:
                fragment = new PathsFragment();
                break;
        }

        return fragment;
    }
}