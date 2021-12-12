package com.apuliacreativehub.eculturetool.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.adapters.PathsAdapter;

public class PathsFragment extends ListFragment {

    private FragmentActivity master;
    private ListAdapter pathsAdapter;
    private boolean isFilterChildOpened = false;

    public void setFilterChildOpened(boolean value) {
        this.isFilterChildOpened = value;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        master = getActivity();

        getParentFragmentManager().setFragmentResultListener("closingBackdrop", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.i("LISTENER", "CALLED");
                isFilterChildOpened = false;

                int lastBack = master.getSupportFragmentManager().getBackStackEntryCount();

                while(lastBack > 1) {
                    master.getSupportFragmentManager().popBackStack();
                    lastBack = master.getSupportFragmentManager().getBackStackEntryCount();
                    Log.i("LISTENER", String.valueOf(lastBack));
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_paths, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int filterId = master.findViewById(R.id.filterPaths).getId();
        if(item.getItemId() == filterId) {
            if(!isFilterChildOpened) {
                setFilterChildOpened(true);
                master.getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.slide_out,
                                R.anim.fade_out
                        )
                        .replace(R.id.container_filter_paths, new PathsFilterFragment())
                        .commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pathsAdapter = new PathsAdapter(getContext());
        setListAdapter(pathsAdapter);
        return inflater.inflate(R.layout.fragment_paths, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textCountResult = master.findViewById(R.id.textListPathsResult);
        if(pathsAdapter.getCount() > 0) {
            textCountResult.setText(textCountResult.getText() + " " + String.valueOf(pathsAdapter.getCount()));
        } else {
            textCountResult.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("DETACHT", "OK");
    }
}