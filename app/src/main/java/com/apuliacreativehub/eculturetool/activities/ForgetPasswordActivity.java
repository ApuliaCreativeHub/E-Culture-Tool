package com.apuliacreativehub.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
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
import com.apuliacreativehub.eculturetool.viewmodels.ForgetPasswordViewModel;

public class ForgetPasswordActivity extends AppCompatActivity {

    private View view;
    private ForgetPasswordViewModel forgetPasswordViewModel;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        view = findViewById(R.id.lytForgetPassword);
        forgetPasswordViewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);

        txtEmail = view.findViewById(R.id.txtEmail);

        if(!forgetPasswordViewModel.getEmail().equals(""))
            txtEmail.setText(forgetPasswordViewModel.getEmail());
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

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                forgetPasswordViewModel.setEmail(editable.toString());
            }
        });

        TextView btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        TextView btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        Button btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            // TODO: Aggiungere query di controllo sul db
            // TODO: Generare la nuova password
            // TODO: Inviare la nuova password tramite mail
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

}