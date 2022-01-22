package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;

public class ManagePlaceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.managePlaceToolbar);
        toolbar.setTitle(R.string.manage_place_screen_title);
        toolbar.inflateMenu(R.menu.top_menu_manage_place);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.editPlaceInformation) {
                TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new EditPlaceFragment());
            }
            return true;
        });

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

}