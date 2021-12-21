package com.apuliacreativehub.eculturetool.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;

public class ForgetPasswordFragment extends Fragment {

    private View view;
    private ForgetPasswordViewModel forgetPasswordViewModel;
    private EditText txtEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgetPasswordViewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);

        txtEmail = view.findViewById(R.id.txtEmail);

        if(!forgetPasswordViewModel.getEmail().equals(""))
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
        btnSignUp.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new RegisterFragment()).commit());

        TextView btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit());

        Button btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            // TODO: Aggiungere query di controllo sul db
            // TODO: Generare la nuova password
            // TODO: Inviare la nuova password tramite mail
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_form_layout, new LoginFragment()).commit();
        });
    }

}