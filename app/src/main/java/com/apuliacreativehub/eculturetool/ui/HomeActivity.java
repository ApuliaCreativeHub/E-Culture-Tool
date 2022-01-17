package com.apuliacreativehub.eculturetool.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    public static final int SHOW_PATHS = 0;
    public static final int SHOW_PLACES = 1;
    public static final int SHOW_PROFILE = 2;

    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //navigateToScreen(getIntent().getExtras().getInt("screen", SHOW_PLACES));

        setContentView(R.layout.home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TokenManager.setTokenFromSharedPreferences(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        navController = Navigation.findNavController(this, R.id.navHostContainer);
        AppBarConfiguration appBarConfiguration= new AppBarConfiguration.Builder(R.id.pathsFragment, R.id.placesFragment, R.id.userFragment).build();
        Toolbar toolbar=findViewById(R.id.homeToolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void navigateToScreen(int screen) {
        switch (screen) {
            case SHOW_PATHS:
                navController.navigate(R.id.pathsFragment);
                break;
            case SHOW_PLACES:
                navController.navigate(R.id.placesFragment);
                break;
            case SHOW_PROFILE:
                SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
                if (!sharedPref.getString("token", "").equals("")) {
                    navController.navigate(R.id.lytProfileDetails);
                } else {
                    navController.navigate(R.id.welcomeFragment);
                }
                break;
        }
    }
}
