package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowPlacesFragment extends Fragment {
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_places, container, false);
        return view;
    }

    public void onStart() {
        super.onStart();

        FloatingActionButton btnCreatePlace = view.findViewById(R.id.btnCreatePlace);
        btnCreatePlace.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.CREATE_PLACE_FRAGMENT)));
    }

}