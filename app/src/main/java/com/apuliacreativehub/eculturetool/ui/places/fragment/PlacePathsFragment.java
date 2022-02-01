package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.places.adapter.PlacePathsAdapter;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.PlacePathsViewModel;

import java.util.List;

public class PlacePathsFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private ConstraintLayout containerNoResult;
    private PlacePathsViewModel placePathsViewModel;
    private final Place place;

    public Observer<RepositoryNotification<List<Path>>> getPlacePathsObserver = new Observer<RepositoryNotification<List<Path>>>() {
        @Override
        public void onChanged(RepositoryNotification<List<Path>> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    placePathsViewModel.setPaths(notification.getData());
                    PlacePathsAdapter mAdapter = new PlacePathsAdapter(notification.getData(), placePathsViewModel.getPlace());
                    mRecyclerView.setAdapter(mAdapter);

                    TextView txtResults = view.findViewById(R.id.txtResults);
                    int item = mAdapter.getItemCount();
                    txtResults.setText(requireContext().getResources().getQuantityString(R.plurals.list_paths_results, item, item));
                    if (item > 0) showResult();
                    else showNoResult();
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "GET_ZONES_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "GET_ZONES_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    public PlacePathsFragment(Place place) {
        this.place = place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place_paths, container, false);
        containerNoResult = view.findViewById(R.id.noResultsLayout);

        placePathsViewModel = new ViewModelProvider(this).get(PlacePathsViewModel.class);
        placePathsViewModel.setPlace(place);

        mRecyclerView = view.findViewById(R.id.listPlacePaths);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.showPlacePathsToolbar);
        toolbar.setTitle(R.string.show_place_default_paths);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        placePathsViewModel.getPlacePathsFromDatabase().observe(getViewLifecycleOwner(), getPlacePathsObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnCreateNewPath = view.findViewById(R.id.btnCreateNewPath);
        btnCreateNewPath.setOnClickListener(view ->
                {
                Place place = new Place();
                place.setId(82);
                TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new CreatePathFragment(place));
                });
    }

    private void showResult() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResult() {
        mRecyclerView.setVisibility(View.GONE);
        containerNoResult.setVisibility(View.VISIBLE);
    }

}