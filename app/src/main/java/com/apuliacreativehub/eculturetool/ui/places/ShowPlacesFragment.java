package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowPlacesFragment extends Fragment {
    private View view;
    protected RecyclerView mRecyclerView;
    protected CardPlaceAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Place> mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_places, container, false);


        this.mDataset = new ArrayList<>();
        this.mDataset.add(new Place("Prova1", "Via Roma, 23", "Primo museo di Modugno", ""));
        this.mDataset.add(new Place("Prova2", "Via Adige, 5b", "Secondo museo di Modugno", ""));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listPlaceCards);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardPlaceAdapter(getActivity(), mDataset);
        mRecyclerView.setAdapter(mAdapter);
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