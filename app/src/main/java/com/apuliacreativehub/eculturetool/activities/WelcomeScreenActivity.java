package com.apuliacreativehub.eculturetool.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        //Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Button login = (Button) findViewById(R.id.login);
        Button signUp = (Button) findViewById(R.id.signUp);
        TextView logAsGuest = (TextView) findViewById(R.id.logAsGuest);
     }

     // On click redirect user to login form
     public void redirectLoginForm(View view){

    }

    // On click redirect user to sign up form
    public void redirectSignUpForm(View view){

    }

    // On click redirect user to homepage in places fragment
    public void redirectHomePagePlaces(View view){
        WelcomeScreenActivity.this.startActivity(new Intent(WelcomeScreenActivity.this, HomeActivity.class));
    }

}