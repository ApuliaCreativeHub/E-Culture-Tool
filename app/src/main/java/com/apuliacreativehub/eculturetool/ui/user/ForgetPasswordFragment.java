package com.apuliacreativehub.eculturetool.ui.user;

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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.dialogfragments.ErrorDialog;

public class ForgetPasswordFragment extends Fragment {

    private View view;
    private ForgetPasswordViewModel forgetPasswordViewModel;
    private EditText txtEmail;
    private AppCompatActivity activity;
    final Observer<RepositoryNotification<Void>> changePasswordObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null || notification.getErrorMessage().isEmpty()) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit();
            } else {
                Log.d("Dialog", "show dialog here");
                new ErrorDialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "CHANGE_PASSWORD_ERROR").show(getChildFragmentManager(), ErrorDialog.TAG);
            }
        } else {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new ErrorDialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "CHANGE_PASSWORD_ERROR").show(getChildFragmentManager(), ErrorDialog.TAG);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (AppCompatActivity) requireActivity();
        Toolbar toolbar = view.findViewById(R.id.forgetPasswordToolbar);
        toolbar.setTitle(R.string.forget_password_screen_title);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        forgetPasswordViewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);

        txtEmail = view.findViewById(R.id.txtEmail);

        if (!forgetPasswordViewModel.getEmail().equals(""))
            txtEmail.setText(forgetPasswordViewModel.getEmail());
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
        btnSignUp.setOnClickListener(view -> activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterFragment()).commit());

        TextView btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit());

        Button btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> forgetPasswordViewModel.changePassword().observe(this, changePasswordObserver));
    }

}