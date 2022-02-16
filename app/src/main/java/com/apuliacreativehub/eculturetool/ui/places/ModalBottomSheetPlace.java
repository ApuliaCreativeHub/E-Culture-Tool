package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apuliacreativehub.eculturetool.BuildConfig;
import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.component.ModalBottomSheetUtils;
import com.apuliacreativehub.eculturetool.ui.component.Utils;
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
    private Button sectionPathLink;
    private View view;
    private final Place place;
    private final Context context;

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
        sectionPathLink = view.findViewById(R.id.sectionPathLink);

        txtName.setText(place.getName());
        txtAddress.setText(place.getAddress());
        txtDescription.setText(place.getDescription());
        Glide.with(context)
                .load(BuildConfig.API_URL + place.getNormalSizeImg()
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imgPlace);
        bottomSheetBehavior = ModalBottomSheetUtils.getBehavior(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        sectionPathLink.setOnClickListener(view -> {
            if (Utils.checkConnection((ConnectivityManager) requireActivity().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                startActivity(new Intent(this.getActivity(), SubActivity.class)
                        .putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.PLACE_PATHS_FRAGMENT)
                        .putExtra("place", place));
            }else{
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        });


    }
}