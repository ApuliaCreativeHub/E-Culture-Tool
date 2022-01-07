package com.apuliacreativehub.eculturetool.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;

public class EditProfileFragment extends Fragment {
    private View view;
    private EditProfileViewModel editProfileViewModel;
    private EditText Name;
    private EditText Surname;
    private EditText Email;
    private EditText Password;
    private EditText ConfirmPassword;
    private Context mcontext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_user, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcontext = getActivity();

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        Name = view.findViewById(R.id.editName);
        Surname = view.findViewById(R.id.editSurname);
        Email = view.findViewById(R.id.editEmail);
        Password = view.findViewById(R.id.editNewPassword);
        ConfirmPassword = view.findViewById(R.id.editConfirmPassword);

        if (mcontext != null) {
            SharedPreferences sharedPref = mcontext.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);

            if(editProfileViewModel.getName().equals("")){
                editProfileViewModel.setName(sharedPref.getString("name", null));
                Name.setText(sharedPref.getString("name", null));
            }else{
                Name.setText(editProfileViewModel.getName());
            }

            if(editProfileViewModel.getSurname().equals("")){
                editProfileViewModel.setSurname(sharedPref.getString("surname", null));
                Surname.setText(sharedPref.getString("surname", null));
            }else{
                Surname.setText(editProfileViewModel.getSurname());
            }

            if(editProfileViewModel.getEmail().equals("")){
                editProfileViewModel.setEmail(sharedPref.getString("email", null));
                Email.setText(sharedPref.getString("email", null));
            }else{
                Email.setText(editProfileViewModel.getEmail());
            }

            if(!editProfileViewModel.getPassword().equals(""))
                Password.setText(editProfileViewModel.getPassword());

            if(!editProfileViewModel.getConfirmPassword().equals(""))
                ConfirmPassword.setText(editProfileViewModel.getConfirmPassword());
        }
    }

    public void onStart() {
        super.onStart();

        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editProfileViewModel.setName(editable.toString());
            }
        });

        Surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editProfileViewModel.setSurname(editable.toString());
            }
        });

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editProfileViewModel.setEmail(editable.toString());
            }
        });

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editProfileViewModel.setPassword(editable.toString());
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
                editProfileViewModel.setConfirmPassword(editable.toString());
            }
        });

        TextView btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(save -> {
            boolean errors = false;

            // Check Name
            if(!editProfileViewModel.isNameCorrect(editProfileViewModel.getName())) {
                Name.setError(getResources().getString(R.string.invalid_name));
                errors = true;
            } else {
                Name.setError(null);
            }

            // Check Surname
            if(!editProfileViewModel.isSurnameCorrect(editProfileViewModel.getSurname())) {
                Surname.setError(getResources().getString(R.string.invalid_surname));
                errors = true;
            } else {
                Surname.setError(null);
            }

            // Check Email
            if(!editProfileViewModel.isEmailCorrect(editProfileViewModel.getEmail())) {
                Email.setError(getResources().getString(R.string.invalid_email));
                errors = true;
            } else {
                Email.setError(null);
            }

            // Check Password
            if(!editProfileViewModel.isPasswordCorrect(editProfileViewModel.getPassword())) {
                Password.setError(getResources().getString(R.string.invalid_password));
                errors = true;
            } else {
                Password.setError(null);
            }

            // Check Confirm Password
            if(!editProfileViewModel.isConfirmPasswordCorrect(editProfileViewModel.getPassword(), editProfileViewModel.getConfirmPassword())) {
                ConfirmPassword.setError(getResources().getString(R.string.invalid_confirm_password));
                errors = true;
            } else {
                ConfirmPassword.setError(null);
            }

            if(!errors) {
                //TODO: implementing user update
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_frame_layout, new ProfileDetailsFragment()).commit();
                //view.findViewById(R.id.lytUser).setVisibility(View.INVISIBLE);
                //view.findViewById(R.id.registrationProgressionBar).setVisibility(View.VISIBLE);
            }


        });
    }
}
