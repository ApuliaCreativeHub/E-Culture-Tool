package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowPlacesFragment extends Fragment {
    private View view;
    protected RecyclerView mRecyclerView;
    protected CardPlaceAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Place> mDataset;
    private ShowPlacesViewModel showPlacesViewModel;

    final Observer<RepositoryNotification<ArrayList<Place>>> getPlacesObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null) {
                mDataset = notification.getData();

                mRecyclerView = (RecyclerView) view.findViewById(R.id.listPlaceCards);
                mLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new CardPlaceAdapter(getActivity(), mDataset);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "LOGIN_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "LOGIN_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_places, container, false);

        showPlacesViewModel = new ViewModelProvider(this).get(ShowPlacesViewModel.class);
        showPlacesViewModel.getPlaces().observe(getViewLifecycleOwner(), getPlacesObserver);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.showPlacesToolbar);
        toolbar.setTitle(R.string.show_places_screen_title);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    public void onStart() {
        super.onStart();

        FloatingActionButton btnCreatePlace = view.findViewById(R.id.btnCreatePlace);
        btnCreatePlace.setOnClickListener(view -> TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new CreatePlaceFragment()));
    }

}