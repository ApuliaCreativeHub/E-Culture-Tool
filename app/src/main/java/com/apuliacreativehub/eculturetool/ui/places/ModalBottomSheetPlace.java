package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.apuliacreativehub.eculturetool.ui.component.ModalBottomSheetUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ModalBottomSheetPlace extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView txtName;
    private TextView txtDescription;
    private TextView txtAddress;
    private ImageView imgPlace;
    private View view;
    private Place place;
    private Context context;

    public ModalBottomSheetPlace(Context context, Place place) {
        this.context = context;
        this.place = place;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.component_modal_bottom_sheet_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtDescription = view.findViewById(R.id.txtDescription);
        imgPlace = view.findViewById(R.id.imgPlace);

        txtName.setText(place.getName());
        txtAddress.setText(place.getAddress());
        txtDescription.setText(place.getDescription());
        Glide.with(context)
                .load("https://hiddenfile.ml/ecultureapi/" + place.getNormalSizeImg()
                        //.load("http://10.0.2.2:8080/" + this.dataSet.get(position)
                        )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imgPlace);
        bottomSheetBehavior = ModalBottomSheetUtil.getBehavior(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button sectionPathLink = view.findViewById(R.id.sectionPathLink);
        sectionPathLink.setOnClickListener(
                view -> startActivity(new Intent(this.getActivity(), SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.PLACE_PATHS_FRAGMENT))
        );
    }
}