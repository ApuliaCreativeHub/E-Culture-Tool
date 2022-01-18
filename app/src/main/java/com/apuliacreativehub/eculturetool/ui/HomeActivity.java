package com.apuliacreativehub.eculturetool.ui;

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
        AppBarConfiguration appBarConfiguration= new AppBarConfiguration.Builder(R.id.pathsFragment, R.id.placesFragment, R.id.userFragment).build();
        Toolbar toolbar=findViewById(R.id.homeToolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}