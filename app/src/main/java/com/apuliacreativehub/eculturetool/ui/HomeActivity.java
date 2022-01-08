package com.apuliacreativehub.eculturetool.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.paths.PathsFragment;
import com.apuliacreativehub.eculturetool.ui.places.PlacesFragment;
import com.apuliacreativehub.eculturetool.ui.user.ProfileDetailsFragment;
import com.apuliacreativehub.eculturetool.ui.user.WelcomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    public static final String SHOW_FRAGMENT = "SHOW_FRAGMENT";
    public static final String USER_FRAGMENT = "USER_FRAGMENT";

    public static final int MENU_ITEM_PLACES = R.id.menuItemPlaces;
    public static final int MENU_ITEM_USER = R.id.menuItemUser;
    public static final int MENU_ITEM_PATHS = R.id.menuItemPaths;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        loadDefaultFragment();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment activeFragment = mapperFragment(item.getItemId());
            return loadFragment(activeFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_frame_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    private void loadDefaultFragment() {
        bottomNavigationView.setSelectedItemId(R.id.menuItemPlaces);
        loadFragment(new PlacesFragment());
    }

    private Fragment mapperFragment(int id) {
        Fragment fragment = null;

        switch(id) {
            case MENU_ITEM_PLACES:
                fragment = new PlacesFragment();
                break;
            case MENU_ITEM_USER:
                SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
                if(!sharedPref.getString("token", "").equals("")){
                    fragment = new ProfileDetailsFragment();
                }else{
                    fragment = new WelcomeFragment();
                }
                break;
            case MENU_ITEM_PATHS:
                fragment = new PathsFragment();
                break;
        }

        return fragment;
    }

}