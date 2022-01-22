package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.apuliacreativehub.eculturetool.ui.component.ListPathsAdapter;

import java.util.ArrayList;

public class PlacePathsFragment extends Fragment {

    private View view;

    private RecyclerView mRecyclerView;
    private ListPathsAdapter mAdapter;
    private LinearLayout containerResults;
    private ConstraintLayout containerNoResult;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Path> mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDataset = new ArrayList<Path>();
        this.mDataset.add(new Path("1", "Percorso standard", "Corteo Milano", null, null, null));
        this.mDataset.add(new Path("2", "Percorso Standard2", "Corteo Roma", null, null, null));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place_paths, container, false);
        containerResults = view.findViewById(R.id.resultsContainerPlacePaths);
        containerNoResult = view.findViewById(R.id.noResultsLayout);
        mRecyclerView = view.findViewById(R.id.listPlacePaths);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListPathsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        ((TextView)view.findViewById(R.id.listResultsItemPathPlace)).setText(String.valueOf(mAdapter.getItemCount()));
        if(mAdapter.getItemCount() > 0) showResult();
        else showNoResult();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        Toolbar toolbar = view.findViewById(R.id.showPlacePathsToolbar);
        toolbar.setTitle(R.string.show_place_default_paths);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnCreateNewPath = view.findViewById(R.id.btnCreateNewPath);
        btnCreateNewPath.setOnClickListener(view -> startActivity(new Intent(this.getActivity(), SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.CREATE_PATH_FRAGMENT)));
    }

    public void showResult() {
        containerResults.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResult() {
        containerNoResult.setVisibility(View.VISIBLE);
    }
}