package com.apuliacreativehub.eculturetool.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.places.CreatePathFragment;
import com.apuliacreativehub.eculturetool.ui.places.CreatePlaceFragment;
import com.apuliacreativehub.eculturetool.ui.places.PlacePathsFragment;
import com.apuliacreativehub.eculturetool.ui.places.ShowPlacesFragment;
import com.apuliacreativehub.eculturetool.ui.user.LoginFragment;
import com.apuliacreativehub.eculturetool.ui.user.RegisterFragment;
import com.apuliacreativehub.eculturetool.ui.user.RegisterInformationFragment;

public class SubActivity extends AppCompatActivity {

    public static final String SHOW_FRAGMENT = "SHOW_FRAGMENT";
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static final String REGISTER_FRAGMENT = "REGISTER_FRAGMENT";
    public static final String REGISTER_INFORMATION_FRAGMENT = "REGISTER_INFORMATION_FRAGMENT";
    public static final String PLACES_FRAGMENT = "PLACES_FRAGMENT";
    public static final String CREATE_PLACE_FRAGMENT = "CREATE_PLACE_FRAGMENT";
    public static final String PLACE_PATHS_FRAGMENT = "PLACE_PATHS_FRAGMENT";
    public static final String CREATE_PATH_FRAGMENT = "CREATE_PATH_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            switch (extras.getString(SHOW_FRAGMENT)) {
                case LOGIN_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit();
                    break;
                case REGISTER_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterFragment()).commit();
                    break;
                case REGISTER_INFORMATION_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterInformationFragment()).commit();
                    break;
                case PLACES_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new ShowPlacesFragment()).commit();
                    break;
                case CREATE_PLACE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new CreatePlaceFragment()).commit();
                    break;
                case PLACE_PATHS_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new PlacePathsFragment()).commit();
                    break;
                case CREATE_PATH_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new CreatePathFragment()).commit();
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
