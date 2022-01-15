package com.apuliacreativehub.eculturetool.ui.paths;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.apuliacreativehub.eculturetool.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class PathsFilterFragment extends Fragment {

    private FragmentActivity master;
    private final Boolean activeFilterPlaces;
    private final Boolean activeFilterName;
    private final Boolean activeFilterDate;
    ImageButton closeFilter;
    MaterialButton applyFilter;
    SwitchMaterial switchSearchName;
    SwitchMaterial switchSearchDate;
    SwitchMaterial switchSearchPlaces;

    public PathsFilterFragment(Boolean activeFilterPlaces, Boolean activeFilterName, Boolean activeFilterDate) {
        this.activeFilterName = activeFilterName;
        this.activeFilterPlaces = activeFilterPlaces;
        this.activeFilterDate = activeFilterDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        master = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_paths_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeFilter = master.findViewById(R.id.btnCloseFilterBackdrop);
        applyFilter = master.findViewById(R.id.btnApplyFilter);
        switchSearchDate = master.findViewById(R.id.switchFilterSearchDate);
        switchSearchName = master.findViewById(R.id.switchFilterSearchName);
        switchSearchPlaces = master.findViewById(R.id.switchFilterSearchPlaces);
        switchSearchDate.setChecked(activeFilterDate);
        switchSearchName.setChecked(activeFilterName);
        switchSearchPlaces.setChecked(activeFilterPlaces);

        closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResult("closingBackdrop", null);
            }
        });

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle temp = new Bundle();
                temp.putBoolean("switchFilterName", switchSearchName.isChecked());
                temp.putBoolean("switchFilterPlaces", switchSearchPlaces.isChecked());
                temp.putBoolean("switchFilterDate", switchSearchDate.isChecked());
                getParentFragmentManager().setFragmentResult("applyFilter", temp);
                closeFilter.callOnClick();
            }
        });
    }
}