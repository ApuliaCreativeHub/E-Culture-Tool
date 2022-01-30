package com.apuliacreativehub.eculturetool.ui.paths;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.ModalBottomSheetUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ModalBottomSheetPaths extends BottomSheetDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private View view;
    private boolean filterPathName = true;
    private boolean filterPlaceName = false;
    private boolean filterPlaceAddress = false;
    private boolean filterObjectInPath = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.component_modal_bottom_sheet_paths, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = ModalBottomSheetUtils.getBehavior(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        CheckBox checkBoxFilterPathName = view.findViewById(R.id.filterPathName);
        CheckBox checkBoxFilterPlaceName = view.findViewById(R.id.filterPlaceName);
        CheckBox checkBoxFilterPlaceAddress = view.findViewById(R.id.filterPlaceAddress);
        CheckBox checkBoxFilterObjectInPath = view.findViewById(R.id.filterObjectInPath);
        Button btnApplyFilters = view.findViewById(R.id.btnApplyFilters);
        btnApplyFilters.setOnClickListener(view  -> {
            filterPathName = checkBoxFilterPathName.isChecked();
            filterPlaceName = checkBoxFilterPlaceName.isChecked();
            filterPlaceAddress = checkBoxFilterPlaceAddress.isChecked();
            filterObjectInPath = checkBoxFilterObjectInPath.isChecked();
            this.dismiss();
        });
    }

    public boolean getFilterPathName() {
        return filterPathName;
    }

    public boolean getFilterPlaceName() {
        return filterPlaceName;
    }

    public boolean getFilterPlaceAddress() {
        return filterPlaceAddress;
    }

    public boolean getFilterObjectInPath() {
        return filterObjectInPath;
    }

}