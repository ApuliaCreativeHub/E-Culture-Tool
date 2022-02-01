package com.apuliacreativehub.eculturetool.ui.paths.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.ui.paths.adapter.PathAdapter;

import java.util.ArrayList;

public class ShowPathFragment extends Fragment {
    private View view;
    private RecyclerView recyclerArtifactsGridView;
    private GridLayoutManager gridLayoutManager;
    private PathAdapter pathAdapter;
    private ArrayList<Object> mDataset;
    private int numberColumn;
    private int orientation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: FETCH ARTIFACT OF PLACE
        Object testArtifact = new Object(100, "AAA", "Opera d'arte antica",  "img1", 1);
        Object testArtifact2 = new Object(101, "BBB", "Opera d'arte antica 2", "img1",2);
        Object testArtifact3 = new Object(102, "CCC", "Opera d'arte antica 3", "img1",3);
        Object testArtifact4 = new Object(103, "DDD", "Opera d'arte antica 4", "img1",3);
        mDataset = new ArrayList<>();
        mDataset.add(testArtifact);
        mDataset.add(testArtifact2);
        mDataset.add(testArtifact3);
        mDataset.add(testArtifact4);
        mDataset.add(testArtifact);
        mDataset.add(testArtifact2);
        mDataset.add(testArtifact3);
        mDataset.add(testArtifact4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            numberColumn = 2;
        } else {
            numberColumn = 1;
        }

        return inflater.inflate(R.layout.fragment_show_path, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        Toolbar toolbar = view.findViewById(R.id.showPathToolbar);
        toolbar.setTitle(R.string.show_path_screen_title);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        setDynamicArtifactRecycleView();
    }

    private void setDynamicArtifactRecycleView() {
        recyclerArtifactsGridView = view.findViewById(R.id.recyclerViewPath);
        gridLayoutManager = new GridLayoutManager(getContext(), numberColumn);
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        } else {
            gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        }
        recyclerArtifactsGridView.setLayoutManager(gridLayoutManager);
        pathAdapter = new PathAdapter(getContext(), mDataset);
        recyclerArtifactsGridView.setAdapter(pathAdapter);
    }

}