package com.apuliacreativehub.eculturetool.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.paths.fragment.ShowPathFragment;

public class LandscapeActivity extends AppCompatActivity {
    public static final String SHOW_FRAGMENT = "SHOW_FRAGMENT";
    public static final String SHOW_PATH_FRAGMENT = "SHOW_PATH_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            switch (extras.getString(SHOW_FRAGMENT)) {
                case SHOW_PATH_FRAGMENT:
                    TransactionHelper.transactionWithoutAddToBackStack(this, R.id.fragment_container_layout, new ShowPathFragment());
                    break;
            }
        }
    }

}