package com.apuliacreativehub.eculturetool.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.apuliacreativehub.eculturetool.R;

public class RegisterInformationFragment extends Fragment {
    private View view;
    private AppCompatActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_information, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.signupInformationToolbar);
        toolbar.setTitle(R.string.signup_information_screen_title);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout, new RegisterFragment()).commit());
    }

}