package com.apuliacreativehub.eculturetool.ui.user.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apuliacreativehub.eculturetool.R;

public class ChangePasswordFragment extends Fragment {
    private EditText Password;
    private EditText ConfirmPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Password = view.findViewById(R.id.editNewPassword);
        ConfirmPassword = view.findViewById(R.id.editConfirmPassword);
    }

    @Override
    public void onStart() {
        super.onStart();

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Bundle result = new Bundle();
                result.putString("changedPassword", editable.toString());
                // The child fragment needs to still set the result on its parent fragment manager
                getParentFragmentManager().setFragmentResult("password", result);

            }
        });

        ConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Bundle result = new Bundle();
                result.putString("confirmPassword", editable.toString());
                // The child fragment needs to still set the result on its parent fragment manager
                getParentFragmentManager().setFragmentResult("password", result);
            }
        });
    }
}
