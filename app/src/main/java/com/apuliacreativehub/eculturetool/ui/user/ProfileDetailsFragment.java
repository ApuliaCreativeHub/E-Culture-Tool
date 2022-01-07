package com.apuliacreativehub.eculturetool.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.HomeActivity;

public class ProfileDetailsFragment extends Fragment {
    private View view;

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
        view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity();
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
            ((TextView)view.findViewById(R.id.txtName)).setText(sharedPref.getString("name", null));
            ((TextView)view.findViewById(R.id.txtSurname)).setText(sharedPref.getString("surname", null));
            ((TextView)view.findViewById(R.id.txtEmail)).setText(sharedPref.getString("email", null));
        }
    }

    public void onStart() {
        super.onStart();

        TextView btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(edit -> {requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_frame_layout, new EditProfileFragment()).commit();});
    }
}