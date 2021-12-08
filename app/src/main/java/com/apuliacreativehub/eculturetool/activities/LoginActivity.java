package com.apuliacreativehub.eculturetool.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.viewmodels.LoginFormViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private LoginFormViewModel lfViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);

        lfViewModel = new ViewModelProvider(this).get(LoginFormViewModel.class);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login", lfViewModel.getEmail().toString());
                Log.i("Login", lfViewModel.getPassword().toString());
                // TODO: Call login method
            }
        });
    }
}
