package com.apuliacreativehub.eculturetool.ui.paths;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.apuliacreativehub.eculturetool.ui.component.ModalBottomSheetUtil;

import java.util.ArrayList;

public class PathsFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayout containerResults;
    private ConstraintLayout containerNoResult;
    private ListPathsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Path> mDataset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDataset = new ArrayList<Path>();
        this.mDataset.add(new Path("1", "Percorso standard", "Corteo Milano", null, null, null));
        this.mDataset.add(new Path("2", "Percorso Standard2", "Corteo Roma", null, null, null));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paths, container, false);
        containerResults = view.findViewById(R.id.resultsContainerPaths);
        containerNoResult = view.findViewById(R.id.noResultsLayoutPaths);
        mRecyclerView = view.findViewById(R.id.userListPaths);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListPathsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        ((TextView)view.findViewById(R.id.listResultsItemPath)).setText(String.valueOf(mAdapter.getItemCount()));

        if(mAdapter.getItemCount() > 0) showResult();
        else showNoResult();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.showUserPathsToolbar);
        toolbar.setTitle(R.string.show_my_paths);
        toolbar.inflateMenu(R.menu.top_menu_paths);
        toolbar.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.filterPaths:
                    ModalBottomSheetPaths modalBottomSheet = new ModalBottomSheetPaths();
                    modalBottomSheet.show(getChildFragmentManager(), ModalBottomSheetUtil.TAG);
                break;
                case R.id.searchPaths:
                    //TODO: send api to fetch data
                break;
            }
            return true;
        });
    }


    public void showResult() {
        containerResults.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResult() {
        containerNoResult.setVisibility(View.VISIBLE);
    }

}
