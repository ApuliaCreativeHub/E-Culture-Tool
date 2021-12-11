package com.apuliacreativehub.eculturetool.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.activities.LoginActivity;

public class WelcomeFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), LoginActivity.class)));
    }

}