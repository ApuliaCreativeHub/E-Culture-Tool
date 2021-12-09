package com.apuliacreativehub.eculturetool.fragments;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.adapters.PathsAdapter;

public class PathsFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListAdapter pathsAdapter = new PathsAdapter(getContext());
        setListAdapter(pathsAdapter);
        return inflater.inflate(R.layout.fragment_paths, container, false);
    }

}