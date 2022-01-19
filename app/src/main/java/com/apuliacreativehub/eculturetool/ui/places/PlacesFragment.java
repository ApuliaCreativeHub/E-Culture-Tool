package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apuliacreativehub.eculturetool.R;

public class PlacesFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_places, menu);
        defineSearchActionBar(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.mapContainer);
        if (mapFragment == null) {
            fragmentTransaction.add(R.id.mapContainer, MapFragment.class, null);
            fragmentTransaction.commit();
        }

        boolean isACurator = false;
        Context context = getActivity();
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
            isACurator = sharedPref.getBoolean("isACurator", false);
        }
        if (isACurator) {
            Button btnManagePlaces = view.findViewById(R.id.btnManagePlaces);
            btnManagePlaces.setVisibility(View.VISIBLE);

            btnManagePlaces.setOnClickListener(managePlaces -> {
                // TODO: Start places management fragment
                Log.d("TODO", "Stub! Start places management fragment");
            });
        }
    }


    private void defineSearchActionBar(@NonNull Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.searchPlaces);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("SUBMIT",  String.valueOf(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}