package com.apuliacreativehub.eculturetool.ui.paths.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.component.ModalBottomSheetUtils;
import com.apuliacreativehub.eculturetool.ui.paths.ModalBottomSheetPaths;
import com.apuliacreativehub.eculturetool.ui.paths.adapter.PathsAdapter;
import com.apuliacreativehub.eculturetool.ui.paths.viewmodel.PathsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PathsFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private static final int FILTER_PATHS = R.id.filterPaths;
    private static final int SEARCH_PATHS = R.id.searchPaths;

    private RecyclerView mRecyclerView;
    private ConstraintLayout containerNoResult;
    private PathsAdapter mAdapter;
    private List<Path> mDataset;
    private Toolbar toolbar;
    private ModalBottomSheetPaths modalBottomSheet;
    private TextView txtResults;
    private PathsViewModel pathsViewModel;
    private View view;

    final Observer<RepositoryNotification<List<Path>>> getYourPlacesObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null) {
                pathsViewModel.setPaths(notification.getData());
                mDataset = new ArrayList<>();
                mDataset.addAll(pathsViewModel.getPaths());

                mAdapter = new PathsAdapter(requireContext(), getParentFragmentManager(), mDataset);
                mRecyclerView.setAdapter(mAdapter);
                show();
                view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
            } else {
                Log.d("Dialog", "show dialog here");
                view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.GET_PLACES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.GET_PLACES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
        }
    };
    private final Observer<RepositoryNotification<Path>> deleteObserver = new Observer<RepositoryNotification<Path>>() {
        @Override
        public void onChanged(RepositoryNotification<Path> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    mDataset.clear();
                    pathsViewModel.removePathFromList(notification.getData().getId());
                    mDataset.addAll(pathsViewModel.getPaths());
                    mAdapter.notifyDataSetChanged();
                    show();
                    view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
                } else {
                    Log.d("Dialog", "show dialog here");
                    view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.DELETE_PLACES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.DELETE_PLACES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paths, container, false);

        modalBottomSheet = new ModalBottomSheetPaths();
        containerNoResult = view.findViewById(R.id.noResultsLayoutPaths);
        txtResults = view.findViewById(R.id.txtResults);

        mRecyclerView = view.findViewById(R.id.userListPaths);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        pathsViewModel = new ViewModelProvider(this).get(PathsViewModel.class);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        view.findViewById(R.id.pathsProgressBar).setVisibility(View.VISIBLE);
        pathsViewModel.getYourPaths().observe(getViewLifecycleOwner(), getYourPlacesObserver);

        MenuItem searchPaths = toolbar.getMenu().findItem(R.id.searchPaths);
        SearchView searchView = (SearchView) searchPaths.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
                mDataset.clear();

                view.findViewById(R.id.pathsProgressBar).setVisibility(View.VISIBLE);
                for (Path path : pathsViewModel.getPaths()) {
                    if ((modalBottomSheet.getFilterPathName() && path.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                            || (modalBottomSheet.getFilterPlaceName() && path.getPlace().getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                            || (modalBottomSheet.getFilterPlaceAddress() && path.getPlace().getAddress().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))))
                        mDataset.add(path);

                    if (modalBottomSheet.getFilterObjectInPath()) {
                        for (int i = 0; i < path.getObjects().size(); i++) {
                            if (path.getObjects().get(i).getName().toLowerCase(Locale.ROOT).contains((query.toLowerCase(Locale.ROOT)))) {
                                mDataset.add(path);
                            }
                        }
                    }
                }

                mAdapter.notifyDataSetChanged();
                view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
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

        getParentFragmentManager().setFragmentResultListener("pathKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                pathsViewModel.setPathIdToRemove(bundle.getInt("pathId"));
                showNoticeDialog();
            }
        });
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_path), DialogTags.DELETE_PATHS_WARNING);
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        try {
            view.findViewById(R.id.pathsProgressBar).setVisibility(View.VISIBLE);
            pathsViewModel.deletePath().observe(this, deleteObserver);
        } catch (NoInternetConnectionException e) {
            view.findViewById(R.id.pathsProgressBar).setVisibility(View.GONE);
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.showUserPathsToolbar);
        toolbar.setTitle(R.string.show_my_paths);
        toolbar.inflateMenu(R.menu.top_menu_paths);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case FILTER_PATHS:
                    modalBottomSheet.show(getChildFragmentManager(), ModalBottomSheetUtils.TAG);
                    break;
                case SEARCH_PATHS:
                    mDataset.clear();
                    mDataset.addAll(pathsViewModel.getPaths());
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
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResult() {
        mRecyclerView.setVisibility(View.GONE);
        containerNoResult.setVisibility(View.VISIBLE);
    }

}