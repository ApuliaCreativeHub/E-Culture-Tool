package com.apuliacreativehub.eculturetool.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TokenManager.setTokenFromSharedPreferences(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        NavController navController = Navigation.findNavController(this, R.id.navHostContainer);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}