package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;

public class EditPlaceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.editPlaceToolbar);
        toolbar.setTitle(R.string.edit_place_screen_title);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_right_bottom_bold);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

}