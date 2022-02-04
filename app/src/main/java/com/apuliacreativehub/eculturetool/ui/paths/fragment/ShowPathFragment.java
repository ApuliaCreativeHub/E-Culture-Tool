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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.ui.paths.adapter.PathAdapter;
import com.apuliacreativehub.eculturetool.ui.paths.viewmodel.ShowPathViewModel;

public class ShowPathFragment extends Fragment {
    private View view;
    private RecyclerView recyclerArtifactsGridView;
    private GridLayoutManager gridLayoutManager;
    private PathAdapter pathAdapter;
    private ShowPathViewModel showPathViewModel;
    private Path path;

    public ShowPathFragment(Path path) {
        this.path = path;
    }

    public ShowPathFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showPathViewModel = new ViewModelProvider(this).get(ShowPathViewModel.class);
        showPathViewModel.setPath(path);
        showPathViewModel.fillDatasetFromPath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showPathViewModel.setOrientation(this.getResources().getConfiguration().orientation);
        if (showPathViewModel.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            showPathViewModel.setNumberColumn(2);
        } else {
            showPathViewModel.setNumberColumn(1);
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
        gridLayoutManager = new GridLayoutManager(getContext(), showPathViewModel.getNumberColumn());
        if (showPathViewModel.getNumberColumn() == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        } else {
            gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        }
        recyclerArtifactsGridView.setLayoutManager(gridLayoutManager);
        pathAdapter = new PathAdapter(getContext(), showPathViewModel.getmDataset());
        recyclerArtifactsGridView.setAdapter(pathAdapter);
    }

}