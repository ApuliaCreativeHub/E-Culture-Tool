package com.apuliacreativehub.eculturetool.ui.user.fragment;

import android.content.Context;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.user.User;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.user.viewmodel.EditProfileViewModel;

import java.util.Objects;

public class EditProfileFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private View view;
    private EditProfileViewModel editProfileViewModel;
    private EditText Name;
    private EditText Surname;
    private EditText Email;
    private TextView btnChangePassword;
    private Context context;
    final Observer<RepositoryNotification<User>> updatingObserver = new Observer<RepositoryNotification<User>>() {
        @Override
        public void onChanged(RepositoryNotification<User> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage()==null || notification.getErrorMessage().isEmpty()) {
                    SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("name");
                    editor.remove("surname");
                    editor.remove("email");
                    editor.putString("name", notification.getData().getName());
                    editor.putString("surname", notification.getData().getSurname());
                    editor.putString("email", notification.getData().getEmail());
                    editor.apply();
                    requireActivity().finish();
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "UPDATING_PROFILE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "UPDATING_PROFILE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Void>> deletingObserver = new Observer<RepositoryNotification<Void>>() {
        @Override
        public void onChanged(RepositoryNotification<Void> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage()==null || notification.getErrorMessage().isEmpty()) {
                    SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.apply();
                    Navigation.findNavController(requireActivity(), R.id.navHostContainer).popBackStack(R.id.placesFragment, false);
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "DELETING_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "DELETING_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getChildFragmentManager()
                .setFragmentResultListener("password", this, (requestKey, bundle) -> {
                    String result = bundle.getString("changedPassword");
                    if(result != null){
                        editProfileViewModel.setPassword(result);
                    }

                    result = bundle.getString("confirmPassword");
                    if(result != null){
                        editProfileViewModel.setConfirmPassword(result);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireContext();

        Toolbar toolbar = view.findViewById(R.id.editProfileToolbar);
        toolbar.setTitle(R.string.edit_profile_screen_title);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        Name = view.findViewById(R.id.editName);
        Surname = view.findViewById(R.id.editSurname);
        Email = view.findViewById(R.id.editEmail);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        EditText Password = view.findViewById(R.id.editNewPassword);
        EditText ConfirmPassword = view.findViewById(R.id.editConfirmPassword);

        SharedPreferences sharedPref = this.context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);

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

        if(btnChangePassword.getText() == getString(R.string.do_not_change_anymore)) {
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

        Button btnSave = view.findViewById(R.id.btnSave);
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

            if(btnChangePassword.getText() == getString(R.string.do_not_change_anymore)) {
                // Check Password
                if (!editProfileViewModel.isPasswordCorrect(editProfileViewModel.getPassword())) {
                    ((EditText) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.changePasswordContainerView)).requireView()
                            .findViewById(R.id.editNewPassword)).setError(getResources().getString(R.string.invalid_password));
                    errors = true;
                } else {
                    ((EditText) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.changePasswordContainerView)).requireView()
                            .findViewById(R.id.editNewPassword)).setError(null);
                }

                // Check Confirm Password
                if (!editProfileViewModel.isConfirmPasswordCorrect(editProfileViewModel.getPassword(), editProfileViewModel.getConfirmPassword())) {
                    ((EditText) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.changePasswordContainerView)).requireView()
                            .findViewById(R.id.editConfirmPassword)).setError(getResources().getString(R.string.invalid_confirm_password));
                    errors = true;
                } else {
                    ((EditText) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.changePasswordContainerView)).requireView()
                            .findViewById(R.id.editConfirmPassword)).setError(null);
                }
            }

            if(!errors) {
                editProfileViewModel.editDetails().observe(this, updatingObserver);
            }
        });

        TextView btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(changePassword -> {
            Fragment changePassswordFragment = getChildFragmentManager().findFragmentById(R.id.changePasswordContainerView);
            if (changePassswordFragment != null){
                getChildFragmentManager().beginTransaction()
                        .remove(changePassswordFragment)
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .commit();
                btnChangePassword.setText(R.string.change_password);
            }else{
                getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .add(R.id.changePasswordContainerView, new ChangePasswordFragment())
                        .setReorderingAllowed(true)
                        .commit();
                btnChangePassword.setText(R.string.do_not_change_anymore);

            }

        });

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(delete -> showNoticeDialog());
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_account), "DELETE_ACCOUNT");
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        editProfileViewModel.deleteUser().observe(this, deletingObserver);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

}