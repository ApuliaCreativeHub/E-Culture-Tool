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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.dialogfragments.UnexpectedExceptionDialog;
import com.apuliacreativehub.eculturetool.ui.dialogfragments.registration.RegistrationErrorDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.net.HttpURLConnection;

public class RegisterFragment extends Fragment {

    private View view;
    private RegisterViewModel registerViewModel;
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private SwitchMaterial sthCurator;

    final Observer<RepositoryNotification<String>> registrationObserver = new Observer<RepositoryNotification<String>>() {
        @Override
        public void onChanged(RepositoryNotification notification) {
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (String.valueOf(notification.getData()).equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit();
                } else if (String.valueOf(notification.getData()).equals(String.valueOf(HttpURLConnection.HTTP_INTERNAL_ERROR))) {
                    Log.d("Dialog", "show dialog here");
                    new RegistrationErrorDialog().show(getChildFragmentManager(), RegistrationErrorDialog.TAG);
                    view.findViewById(R.id.registrationProgressionBar).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.lytUser).setVisibility(View.VISIBLE);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new UnexpectedExceptionDialog().show(getChildFragmentManager(), UnexpectedExceptionDialog.TAG);
                view.findViewById(R.id.registrationProgressionBar).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.lytUser).setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtSurname = view.findViewById(R.id.txtSurname);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        sthCurator = view.findViewById(R.id.sthCurator);

        if (!registerViewModel.getName().equals(""))
            txtName.setText(registerViewModel.getName());

        if(!registerViewModel.getSurname().equals(""))
            txtSurname.setText(registerViewModel.getSurname());

        if(!registerViewModel.getEmail().equals(""))
            txtEmail.setText(registerViewModel.getEmail());

        if(!registerViewModel.getPassword().equals(""))
            txtPassword.setText(registerViewModel.getPassword());

        if(!registerViewModel.getConfirmPassword().equals(""))
            txtConfirmPassword.setText(registerViewModel.getConfirmPassword());

        if(!registerViewModel.getIsCurator() == Boolean.parseBoolean(null))
            sthCurator.setChecked(registerViewModel.getIsCurator());
    }

    @Override
    public void onStart() {
        super.onStart();

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setName(editable.toString());
            }
        });

        txtSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setSurname(editable.toString());
            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setEmail(editable.toString());
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
                registerViewModel.setPassword(editable.toString());
            }
        });

        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setConfirmPassword(editable.toString());
            }
        });

        sthCurator.setOnCheckedChangeListener((buttonView, isChecked) -> registerViewModel.setIsCurator(isChecked));

        TextView btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit());

        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(OnClickListener -> {
            boolean errors = false;

            // Check Name
            if(!registerViewModel.isNameCorrect(registerViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            // Check Surname
            if(!registerViewModel.isSurnameCorrect(registerViewModel.getSurname())) {
                txtSurname.setError(getResources().getString(R.string.invalid_surname));
                errors = true;
            } else {
                txtSurname.setError(null);
            }

            // Check Email
            if(!registerViewModel.isEmailCorrect(registerViewModel.getEmail())) {
                txtEmail.setError(getResources().getString(R.string.invalid_email));
                errors = true;
            } else {
                txtEmail.setError(null);
            }

            // Check Password
            if(!registerViewModel.isPasswordCorrect(registerViewModel.getPassword())) {
                txtPassword.setError(getResources().getString(R.string.invalid_password));
                errors = true;
            } else {
                txtPassword.setError(null);
            }

            // Check Confirm Password
            if(!registerViewModel.isConfirmPasswordCorrect(registerViewModel.getPassword(), registerViewModel.getConfirmPassword())) {
                txtConfirmPassword.setError(getResources().getString(R.string.invalid_confirm_password));
                errors = true;
            } else {
                txtConfirmPassword.setError(null);
            }

            if(!errors) {
                registerViewModel.registerUser().observe(this, registrationObserver);
                view.findViewById(R.id.lytUser).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.registrationProgressionBar).setVisibility(View.VISIBLE);
            }
        });
    }

}