package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.SubActivity;

public class PlacesFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, MapFragment.class, null);
        fragmentTransaction.commit();

        boolean isACurator = false;
        Context context = getActivity();
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
            isACurator = sharedPref.getBoolean("isACurator", false);
        }
        if (isACurator) {
            Button btnManagePlaces = view.findViewById(R.id.btnManagePlaces);
            btnManagePlaces.setVisibility(View.VISIBLE);
            btnManagePlaces.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.PLACES_FRAGMENT)));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar=view.findViewById(R.id.placesToolbar);
        toolbar.setTitle(R.string.places_screen_title);
        AppCompatActivity activity=(AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar= activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

}