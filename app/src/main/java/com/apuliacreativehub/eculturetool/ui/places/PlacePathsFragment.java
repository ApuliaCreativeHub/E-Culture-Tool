package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.ui.component.ListPathsAdapter;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;

import java.util.ArrayList;

public class PlacePathsFragment extends Fragment {

    private View view;

    protected RecyclerView mRecyclerView;
    protected ListPathsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Path> mDataset;

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
        mRecyclerView = view.findViewById(R.id.listPlacePaths);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListPathsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView)view.findViewById(R.id.listResultsItemPathPlace)).setText(String.valueOf(mAdapter.getItemCount()));

        Toolbar toolbar = view.findViewById(R.id.showPlacePathsToolbar);
        toolbar.setTitle(R.string.show_place_default_paths);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_right_bottom_bold);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnCreateNewPath = view.findViewById(R.id.btnCreateNewPath);
        btnCreateNewPath.setOnClickListener(view -> TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new CreatePathFragment()));
    }
}