package com.apuliacreativehub.eculturetool.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.apuliacreativehub.eculturetool.R;

public class FormActivity extends AppCompatActivity {

    public static final String SHOW_FRAGMENT = "SHOW_FRAGMENT";
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static final String REGISTER_FRAGMENT = "REGISTER_FRAGMENT";
    public static final String REGISTER_INFORMATION_FRAGMENT = "REGISTER_INFORMATION_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            switch(extras.getString(SHOW_FRAGMENT)) {
                case LOGIN_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit();
                    break;
                case REGISTER_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterFragment()).commit();
                    break;
                case REGISTER_INFORMATION_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterInformationFragment()).commit();
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}