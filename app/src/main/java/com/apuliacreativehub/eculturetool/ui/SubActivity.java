package com.apuliacreativehub.eculturetool.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.places.PlacePathsFragment;
import com.apuliacreativehub.eculturetool.ui.places.ShowPlacesFragment;
import com.apuliacreativehub.eculturetool.ui.user.EditProfileFragment;
import com.apuliacreativehub.eculturetool.ui.user.LoginFragment;
import com.apuliacreativehub.eculturetool.ui.user.RegisterFragment;
import com.apuliacreativehub.eculturetool.ui.user.RegisterInformationFragment;

public class SubActivity extends AppCompatActivity {
    public static final String SHOW_FRAGMENT = "SHOW_FRAGMENT";
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static final String REGISTER_FRAGMENT = "REGISTER_FRAGMENT";
    public static final String REGISTER_INFORMATION_FRAGMENT = "REGISTER_INFORMATION_FRAGMENT";
    public static final String PLACES_FRAGMENT = "PLACES_FRAGMENT";
    public static final String PLACE_PATHS_FRAGMENT = "PLACE_PATHS_FRAGMENT";
    public static final String EDIT_PROFILE_FRAGMENT = "EDIT_PROFILE_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            switch (extras.getString(SHOW_FRAGMENT)) {
                case LOGIN_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new LoginFragment());
                    break;
                case REGISTER_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new RegisterFragment());
                    break;
                case REGISTER_INFORMATION_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new RegisterInformationFragment());
                    break;
                case PLACES_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new ShowPlacesFragment());
                    break;
                case PLACE_PATHS_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new PlacePathsFragment());
                    break;
                case EDIT_PROFILE_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new EditProfileFragment());
                    break;
            }
        }
    }
}