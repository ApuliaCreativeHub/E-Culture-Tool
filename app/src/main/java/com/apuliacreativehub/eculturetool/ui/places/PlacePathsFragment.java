package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.ui.component.ListPathsAdapter;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;

import java.util.ArrayList;

public class PlacePathsFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private ConstraintLayout containerNoResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place_paths, container, false);
        containerNoResult = view.findViewById(R.id.noResultsLayout);

        mRecyclerView = view.findViewById(R.id.listPlacePaths);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // TODO: Read Curator Paths API
        ArrayList<Path> mDataset = new ArrayList<>();
        mDataset.add(new Path(1, "Percorso 1", "Museo 1", "Indirizzo 1", null));
        mDataset.add(new Path(2, "Percorso 2", "Museo 2", "Indirizzo 2", null));
        mDataset.add(new Path(3, "Percorso 3", "Museo 3", "Indirizzo 3", null));

        ListPathsAdapter mAdapter = new ListPathsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        TextView txtResults = view.findViewById(R.id.txtResults);
        int item = mAdapter.getItemCount();
        txtResults.setText(requireContext().getResources().getQuantityString(R.plurals.list_paths_results, item, item));
        if(item > 0) showResult();
        else showNoResult();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.showPlacePathsToolbar);
        toolbar.setTitle(R.string.show_place_default_paths);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnCreateNewPath = view.findViewById(R.id.btnCreateNewPath);
        btnCreateNewPath.setOnClickListener(view -> TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new CreatePathFragment()));
    }

    private void showResult() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResult() {
        mRecyclerView.setVisibility(View.GONE);
        containerNoResult.setVisibility(View.VISIBLE);
    }

}