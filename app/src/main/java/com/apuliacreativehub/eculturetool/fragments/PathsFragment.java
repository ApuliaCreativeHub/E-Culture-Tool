package com.apuliacreativehub.eculturetool.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.adapters.PathsAdapter;

public class PathsFragment extends ListFragment {

    private FragmentActivity master;
    private PathsAdapter pathsAdapter;
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
                isFilterChildOpened = false;
            }
        });

        getParentFragmentManager().setFragmentResultListener("applyFilter", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                pathsAdapter.setFilterDate(result.getBoolean("switchFilterDate"));
                pathsAdapter.setFilterPlaces(result.getBoolean("switchFilterPlaces"));
                pathsAdapter.setFilterName(result.getBoolean("switchFilterName"));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_paths, menu);
        defineSearchActionBar(menu);
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
                        .replace(R.id.container_filter_paths,
                                new PathsFilterFragment(pathsAdapter.getFilterPlaces(), pathsAdapter.getFilterName(), pathsAdapter.getFilterDate()))
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
        setCountResult();
    }

    private void defineSearchActionBar(@NonNull Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.searchPaths);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pathsAdapter.applyFilter(query);
                setListAdapter(pathsAdapter);
                setCountResult();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    pathsAdapter.restoreAll();
                    setListAdapter(pathsAdapter);
                    setCountResult();
                }
                return true;
            }
        });
    }

    private void setCountResult() {
        TextView textCountResult = master.findViewById(R.id.textListPathsResult);
        if(pathsAdapter.getCount() > 0) {
            textCountResult.setVisibility(View.VISIBLE);
            textCountResult.setText(getString(R.string.list_paths_results) + " " + String.valueOf(pathsAdapter.getCount()));
            ((ImageView) master.findViewById(R.id.frameNotFoundPaths)).setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) master.findViewById(R.id.frameNotFoundPaths)).setVisibility(View.VISIBLE);
            textCountResult.setVisibility(View.INVISIBLE);
        }
    }
}