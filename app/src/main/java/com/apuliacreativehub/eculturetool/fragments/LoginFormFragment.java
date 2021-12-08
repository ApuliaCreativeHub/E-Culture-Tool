package com.apuliacreativehub.eculturetool.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.viewmodels.LoginFormViewModel;

public class LoginFormFragment extends Fragment {
    private LoginFormViewModel viewModel;
    private EditText emailET;
    private EditText passwordET;

    public LoginFormFragment(){
        super(R.layout.login_form_fields);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailET=view.findViewById(R.id.editTextEmail);
        passwordET=view.findViewById(R.id.editTextPassword);
        viewModel=new ViewModelProvider(requireActivity()).get(LoginFormViewModel.class);

        if(!viewModel.getEmail().equals("")){
            emailET.setText(viewModel.getEmail());
        }

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.setEmail(editable.toString());
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.setPassword(editable.toString());
            }
        });

    }

}
