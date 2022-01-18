package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.apuliacreativehub.eculturetool.R;

public class WelcomeFragment extends Fragment {
    private Activity activity;
    private View view;
    private final ActivityResultLauncher<Intent> loginDoneCallback = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> Navigation.findNavController(activity, R.id.navHostContainer).popBackStack(R.id.userFragment, false));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(activity, R.id.navHostContainer).popBackStack(R.id.placesFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }

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
        btnLogin.setOnClickListener(view -> loginDoneCallback.launch(new Intent(this.getActivity(), FormActivity.class).putExtra(FormActivity.SHOW_FRAGMENT, FormActivity.LOGIN_FRAGMENT)));

        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), FormActivity.class).putExtra(FormActivity.SHOW_FRAGMENT, FormActivity.REGISTER_FRAGMENT)));

        TextView btnRegisterInformation = view.findViewById(R.id.btnRegisterInformation);
        btnRegisterInformation.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), FormActivity.class).putExtra(FormActivity.SHOW_FRAGMENT, FormActivity.REGISTER_INFORMATION_FRAGMENT)));
    }

}