package com.apuliacreativehub.eculturetool.ui.paths;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import java.util.Locale;

public class PathsFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayout containerResults;
    private ConstraintLayout containerNoResult;
    private ListPathsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Path> mDataset;
    private ArrayList<Path> paths;
    private Toolbar toolbar;
    private ModalBottomSheetPaths modalBottomSheet;
    private TextView txtResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paths, container, false);

        modalBottomSheet = new ModalBottomSheetPaths();
        containerResults = view.findViewById(R.id.resultsContainerPaths);
        containerNoResult = view.findViewById(R.id.noResultsLayoutPaths);

        mRecyclerView = view.findViewById(R.id.userListPaths);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // TODO: Read Paths API
        paths = new ArrayList<>();
        paths.add(new Path(1, "Percorso 1", "Museo 1", "Indirizzo 1", null));
        paths.add(new Path(2, "Percorso 2", "Museo 2", "Indirizzo 1", null));
        paths.add(new Path(3, "Percorso 3", "Museo 3", "Indirizzo 3", null));
        mDataset = new ArrayList<>();
        mDataset.addAll(paths);

        mAdapter = new ListPathsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        txtResults = view.findViewById(R.id.txtResults);
        show();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        MenuItem searchPaths = toolbar.getMenu().findItem(R.id.searchPaths);
        SearchView searchView = (SearchView) searchPaths.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mDataset.clear();

                for(Path path : paths) {
                    if((modalBottomSheet.getFilterPathName() && path.getPathName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                            || (modalBottomSheet.getFilterPlaceName() && path.getPlaceName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                            || (modalBottomSheet.getFilterPlaceAddress() && path.getPlaceAddress().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))))
                        mDataset.add(path);
                }

                if(modalBottomSheet.getFilterObjectInPath()) {
                    // TODO: Perform BackEnd research if we want or delete this fragment of code and
                    //       CheckBox in component_modal_bottom_sheet_paths.xml
                }

                mAdapter.notifyDataSetChanged();
                show();

                if(!searchView.isIconified())
                    searchView.setIconified(true);
                searchPaths.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.showUserPathsToolbar);
        toolbar.setTitle(R.string.show_my_paths);
        toolbar.inflateMenu(R.menu.top_menu_paths);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.filterPaths:
                    modalBottomSheet.show(getChildFragmentManager(), ModalBottomSheetUtil.TAG);
                    break;
                case R.id.searchPaths:
                    mDataset.clear();
                    mDataset.addAll(paths);
                    mAdapter.notifyDataSetChanged();
                    show();
                    break;
            }
            return true;
        });
    }

    private void show() {
        int item = mAdapter.getItemCount();
        txtResults.setText(requireContext().getResources().getQuantityString(R.plurals.list_paths_results, item, item));
        if(item > 0) showResult();
        else showNoResult();
    }

    private void showResult() {
        containerResults.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResult() {
        mRecyclerView.setVisibility(View.GONE);
        containerNoResult.setVisibility(View.VISIBLE);
    }

}