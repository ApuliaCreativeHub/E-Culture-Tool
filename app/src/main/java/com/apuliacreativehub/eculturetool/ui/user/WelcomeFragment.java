package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apuliacreativehub.eculturetool.R;

public class WelcomeFragment extends Fragment {
    private Activity activity;

    private View view;
    private final ActivityResultLauncher<Intent> loginDoneCallback = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        NavController navController = Navigation.findNavController(activity, R.id.navHostContainer);
        navController.popBackStack(R.id.userFragment, false);
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            loginDoneCallback.launch(new Intent(this.getActivity(), FormActivity.class).putExtra(FormActivity.SHOW_FRAGMENT, FormActivity.LOGIN_FRAGMENT));
        });

        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), FormActivity.class).putExtra(FormActivity.SHOW_FRAGMENT, FormActivity.REGISTER_FRAGMENT)));

        TextView btnRegisterInformation = view.findViewById(R.id.btnRegisterInformation);
        btnRegisterInformation.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), FormActivity.class).putExtra(FormActivity.SHOW_FRAGMENT, FormActivity.REGISTER_INFORMATION_FRAGMENT)));
    }
}