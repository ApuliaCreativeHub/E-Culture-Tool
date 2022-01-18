package com.apuliacreativehub.eculturetool.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.apuliacreativehub.eculturetool.R;

public class UserFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
        assert getParentFragment() != null;
        if (sharedPref.getString("token", "").equals("")) {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_userFragment_to_welcomeFragment);
        } else {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_userFragment_to_profileDetailsFragment);
        }
    }

}