package com.apuliacreativehub.eculturetool.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryCallback;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;

import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private View view;
    private RegisterViewModel registerViewModel;
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private SwitchMaterial sthCurator;
    RepositoryCallback<Void> callback = new RepositoryCallback<Void>() {
        @Override
        public void onComplete(Response<Void> response) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(response.code()));
        }

        @Override
        public void onException(IOException ioe) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + ioe.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        view = findViewById(R.id.lytRegister);
        RegisterViewModelFactory registerViewModelFactory = new RegisterViewModelFactory(getApplication(), callback);
        registerViewModel = new ViewModelProvider(this, registerViewModelFactory).get(RegisterViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtSurname = view.findViewById(R.id.txtSurname);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        sthCurator = view.findViewById(R.id.sthCurator);

        if (!registerViewModel.getName().equals(""))
            txtName.setText(registerViewModel.getName());

        if(!registerViewModel.getSurname().equals(""))
            txtSurname.setText(registerViewModel.getSurname());

        if(!registerViewModel.getEmail().equals(""))
            txtEmail.setText(registerViewModel.getEmail());

        if(!registerViewModel.getPassword().equals(""))
            txtPassword.setText(registerViewModel.getPassword());

        if(!registerViewModel.getConfirmPassword().equals(""))
            txtConfirmPassword.setText(registerViewModel.getConfirmPassword());

        if(!registerViewModel.getIsCurator() == Boolean.parseBoolean(null))
            sthCurator.setChecked(registerViewModel.getIsCurator());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setName(editable.toString());
            }
        });

        txtSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setSurname(editable.toString());
            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setEmail(editable.toString());
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setPassword(editable.toString());
            }
        });

        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setConfirmPassword(editable.toString());
            }
        });

        sthCurator.setOnCheckedChangeListener((buttonView, isChecked) -> registerViewModel.setIsCurator(isChecked));

        TextView btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(OnClickListener -> {
            boolean errors = false;

            // Check Name
            if(!registerViewModel.isNameCorrect(registerViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            // Check Surname
            if(!registerViewModel.isSurnameCorrect(registerViewModel.getSurname())) {
                txtSurname.setError(getResources().getString(R.string.invalid_surname));
                errors = true;
            } else {
                txtSurname.setError(null);
            }

            // Check Email
            if(!registerViewModel.isEmailCorrect(registerViewModel.getEmail())) {
                txtEmail.setError(getResources().getString(R.string.invalid_email));
                errors = true;
            } else {
                txtEmail.setError(null);
            }

            // Check Password
            if(!registerViewModel.isPasswordCorrect(registerViewModel.getPassword())) {
                txtPassword.setError(getResources().getString(R.string.invalid_password));
                errors = true;
            } else {
                txtPassword.setError(null);
            }

            // Check Confirm Password
            if(!registerViewModel.isConfirmPasswordCorrect(registerViewModel.getPassword(), registerViewModel.getConfirmPassword())) {
                txtConfirmPassword.setError(getResources().getString(R.string.invalid_confirm_password));
                errors = true;
            } else {
                txtConfirmPassword.setError(null);
            }

            if(!errors) {
                registerViewModel.registerUser();
                // TODO: Add an indeterminate progress bar during the HTTP request
                // TODO: Move startActivity to the request success callback
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

}