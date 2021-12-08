package com.apuliacreativehub.eculturetool.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.viewmodels.SignUpFormViewModel;

public class SignUpActivity extends AppCompatActivity {
    private Button signUpButton;
    private SignUpFormViewModel sfViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_form);

        sfViewModel = new ViewModelProvider(this).get(SignUpFormViewModel.class);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login", sfViewModel.getName().toString());
                Log.i("Login", sfViewModel.getSurname().toString());
                Log.i("Login", sfViewModel.getAge().toString());
                Log.i("Login", sfViewModel.getEmail().toString());
                Log.i("Login", sfViewModel.getPassword().toString());
                Log.i("Login", sfViewModel.getPassword().toString());
                // TODO: Call signup method
            }
        });
    }
}
