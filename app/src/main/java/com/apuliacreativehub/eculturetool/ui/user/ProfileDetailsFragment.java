package com.apuliacreativehub.eculturetool.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;

public class ProfileDetailsFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private View view;
    private LogoutViewModel logoutViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireActivity(), R.id.navHostContainer).popBackStack(R.id.placesFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar=view.findViewById(R.id.profileToolbar);
        toolbar.setTitle(R.string.user_screen_title);
        AppCompatActivity activity=(AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

        SharedPreferences sharedPref = activity.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
        ((TextView)view.findViewById(R.id.txtName)).setText(sharedPref.getString("name", null));
        ((TextView)view.findViewById(R.id.txtSurname)).setText(sharedPref.getString("surname", null));
        ((TextView)view.findViewById(R.id.txtEmail)).setText(sharedPref.getString("email", null));
    }

    public void onStart() {
        super.onStart();

        Button btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.EDIT_PROFILE_FRAGMENT)));

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(logout -> showNoticeDialog());
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_exit_account), "LOGOUT_ACCOUNT");
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        Context context = getActivity();
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            logoutViewModel.logoutUser();
            Navigation.findNavController(requireActivity(), R.id.navHostContainer).popBackStack(R.id.placesFragment, false);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

}