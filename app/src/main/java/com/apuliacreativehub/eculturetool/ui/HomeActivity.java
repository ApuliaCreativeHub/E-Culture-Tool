package com.apuliacreativehub.eculturetool.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.UserPreferencesManager;
import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;
import com.apuliacreativehub.eculturetool.di.ECultureTool;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserPreferencesManager.setUserInfoFromSharedPreferences(this);
        ((ECultureTool) getApplication()).localDatabase = Room.databaseBuilder(this, LocalDatabase.class, "ECultureTool").enableMultiInstanceInvalidation().build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        NavController navController = Navigation.findNavController(this, R.id.navHostContainer);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}