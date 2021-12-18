package com.apuliacreativehub.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.apuliacreativehub.eculturetool.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.fragments.PathsFragment;
import com.apuliacreativehub.eculturetool.fragments.PlacesFragment;
import com.apuliacreativehub.eculturetool.fragments.WelcomeFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public static final int menuItemPlaces = R.id.menuItemPlaces;
    public static final int menuItemUser = R.id.menuItemUser;
    public static final int menuItemPaths = R.id.menuItemPaths;

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
            case menuItemPlaces:
                fragment = new PlacesFragment();
                break;
            case menuItemUser:
                // Qua bisogna aggiungere l'if per la sessione:
                // se l'utente non è loggato dev'essere caricato il WelcomeFragment
                // altrimenti dev'essere caricare l'UserFragment
                fragment = new WelcomeFragment();
                // fragment = new UserFragment();
                break;
            case menuItemPaths:
                fragment = new PathsFragment();
                break;
        }

        return fragment;
    }

}