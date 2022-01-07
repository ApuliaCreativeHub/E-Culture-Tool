package com.apuliacreativehub.eculturetool.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.TokenManager;
import com.apuliacreativehub.eculturetool.data.UuidManager;
import com.apuliacreativehub.eculturetool.data.entity.UserWithToken;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.HomeActivity;
import com.apuliacreativehub.eculturetool.ui.dialogfragments.ErrorDialog;

public class LoginFragment extends Fragment {
    private View view;
    private LoginViewModel loginViewModel;
    private EditText txtEmail;
    private EditText txtPassword;
    final Observer<RepositoryNotification<UserWithToken>> loginObserver = new Observer<RepositoryNotification<UserWithToken>>() {
        @Override
        public void onChanged(RepositoryNotification<UserWithToken> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    // Add token to shared preferences
                    Context context = getActivity();
                    if (context != null) {
                        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", notification.getData().getToken().getToken());
                        TokenManager.setToken(notification.getData().getToken().getToken());
                        editor.putBoolean("isLogged", true);
                        editor.putString("name", notification.getData().getUser().getName());
                        editor.putString("surname", notification.getData().getUser().getSurname());
                        editor.putString("email", notification.getData().getUser().getEmail());
                        editor.putBoolean("isACurator", notification.getData().getUser().isACurator());
                        editor.apply();
                        startActivity(new Intent(getActivity(), HomeActivity.class).putExtra(HomeActivity.SHOW_FRAGMENT, HomeActivity.USER_FRAGMENT));
                    }
                } else {
                    Log.d("Dialog", "show dialog here");
                    new ErrorDialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "LOGIN_ERROR").show(getChildFragmentManager(), ErrorDialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new ErrorDialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "LOGIN_EXCEPTION").show(getChildFragmentManager(), ErrorDialog.TAG);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    private Context mcontext;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcontext = getContext();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        txtEmail = view.findViewById(R.id.txtEmail);
        txtPassword = view.findViewById(R.id.txtPassword);

        if (!loginViewModel.getEmail().equals(""))
            txtEmail.setText(loginViewModel.getEmail());

        if (!loginViewModel.getPassword().equals(""))
            txtPassword.setText(loginViewModel.getPassword());
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
                loginViewModel.setEmail(editable.toString());
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
                loginViewModel.setPassword(editable.toString());
            }
        });

        TextView btnForgetPassword = view.findViewById(R.id.btnForgetPassword);
        btnForgetPassword.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new ForgetPasswordFragment()).commit());

        TextView btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterFragment()).commit());

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            loginViewModel.setUuid(UuidManager.getUuid(getActivity(), getString(R.string.login_shared_preferences)));
            loginViewModel.loginUser().observe(this, loginObserver);
        });
    }

}