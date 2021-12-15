package com.apuliacreativehub.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.viewmodels.RegisterViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class RegisterActivity extends AppCompatActivity {

    private View view;
    private RegisterViewModel registerViewModel;
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private SwitchMaterial sthCurator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        view = findViewById(R.id.lytRegister);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtSurname = view.findViewById(R.id.txtSurname);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        sthCurator = view.findViewById(R.id.sthCurator);

        if(!registerViewModel.getName().equals(""))
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

        TextView btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

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

        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(OnClickListener -> {
            // Check Name
            switch(registerViewModel.checkName(registerViewModel.getName())) {
                case "empty":
                    txtName.setError(getResources().getString(R.string.empty));
                    break;
                case "name_too_long_or_short":
                    txtName.setError(getResources().getString(R.string.name_too_long_or_short));
                    break;
                case "invalid_name":
                    txtName.setError(getResources().getString(R.string.invalid_name));
                    break;
                default:
                    txtName.setError(null);
                    break;
            }

            // Check Surname
            switch(registerViewModel.checkSurname(registerViewModel.getSurname())) {
                case "empty":
                    txtSurname.setError(getResources().getString(R.string.empty));
                    break;
                case "surname_too_long_or_short":
                    txtSurname.setError(getResources().getString(R.string.surname_too_long_or_short));
                    break;
                case "invalid_surname":
                    txtSurname.setError(getResources().getString(R.string.invalid_surname));
                    break;
                default:
                    txtSurname.setError(null);
                    break;
            }

            // Check Email
            switch(registerViewModel.checkEmail(registerViewModel.getEmail())) {
                case "empty":
                    txtEmail.setError(getResources().getString(R.string.empty));
                    break;
                case "invalid_email":
                    txtEmail.setError(getResources().getString(R.string.invalid_email));
                    break;
                default:
                    txtEmail.setError(null);
                    break;
            }

            // Check Password
            switch(registerViewModel.checkPassword(registerViewModel.getPassword())) {
                case "empty":
                    txtPassword.setError(getResources().getString(R.string.empty));
                    break;
                case "invalid_password":
                    txtPassword.setError(getResources().getString(R.string.invalid_password));
                    break;
                default:
                    txtPassword.setError(null);
                    break;
            }

            // Check Confirm Password
            switch(registerViewModel.checkConfirmPassword(registerViewModel.getPassword(), registerViewModel.getConfirmPassword())) {
                case "empty":
                    txtConfirmPassword.setError(getResources().getString(R.string.empty));
                    break;
                case "invalid_confirm_password":
                    txtConfirmPassword.setError(getResources().getString(R.string.invalid_confirm_password));
                    break;
                default:
                    txtConfirmPassword.setError(null);
                    break;
            }
        });
    }

}