package com.apuliacreativehub.eculturetool.ui.user.fragment;

import android.app.Activity;
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

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.UserPreferencesManager;
import com.apuliacreativehub.eculturetool.data.UuidManager;
import com.apuliacreativehub.eculturetool.data.entity.user.UserWithToken;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.user.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private View view;
    private LoginViewModel loginViewModel;
    private boolean shouldShowSendPathDialog = false;
    private EditText txtEmail;
    private EditText txtPassword;
    final Observer<RepositoryNotification<UserWithToken>> loginObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        view.findViewById(R.id.loginProgressBar).setVisibility(View.GONE);
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
                    UserPreferencesManager.setToken(notification.getData().getToken().getToken());
                    editor.putBoolean("isLogged", true);
                    editor.putInt("id", notification.getData().getUser().getId());
                    editor.putString("name", notification.getData().getUser().getName());
                    editor.putString("surname", notification.getData().getUser().getSurname());
                    editor.putString("email", notification.getData().getUser().getEmail());
                    editor.putBoolean("isACurator", notification.getData().getUser().isACurator());
                    UserPreferencesManager.setUserInfo(
                            notification.getData().getUser().getId(),
                            notification.getData().getUser().getName(),
                            notification.getData().getUser().getSurname(),
                            notification.getData().getUser().getEmail(),
                            notification.getData().getUser().isACurator());
                    editor.apply();
                    if (shouldShowSendPathDialog) {
                        ConfirmationDialog sendPathsDialog = new ConfirmationDialog(getString(R.string.save_to_account_dialog_title), getString(R.string.warning_save_visitor_paths_to_account), DialogTags.SAVE_VISITOR_PATHS_WARNING);
                        sendPathsDialog.setCustomPositiveButtonLabel(R.string.confirm_save_visitor_paths_to_account);
                        sendPathsDialog.setCustomNegativeButtonLabel(R.string.cancel_save_visitor_paths_to_account);
                        sendPathsDialog.show(getChildFragmentManager(), Dialog.TAG);
                    } else {
                        finishActivity();
                    }
                }
            } else {
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.LOGIN_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.LOGIN_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
        }
    };

    final Observer<Integer> countVisitorPathsObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer numPaths) {
            if (numPaths > 0) shouldShowSendPathDialog = true;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.loginToolbar);
        toolbar.setTitle(R.string.login_screen_title);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.checkForVisitorPaths().observe(getViewLifecycleOwner(), countVisitorPathsObserver);

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
        btnForgetPassword.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout, new ForgetPasswordFragment()).commit());

        TextView btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout, new RegisterFragment()).commit());

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            view.findViewById(R.id.loginProgressBar).setVisibility(View.VISIBLE);
            loginViewModel.setUuid(UuidManager.getUuid(getActivity(), getString(R.string.login_shared_preferences)));
            loginViewModel.loginUser().observe(this, loginObserver);
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        loginViewModel.importVisitorPathsToAccount();
        finishActivity();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d("SAVE_VISITOR_PATHS", "Visitor paths saving aborted by the user");
        finishActivity();
    }

    private void finishActivity() {
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
    }
}