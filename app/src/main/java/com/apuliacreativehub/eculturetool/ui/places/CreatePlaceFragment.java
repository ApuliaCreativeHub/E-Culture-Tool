package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.ErrorDialog;

@SuppressWarnings("deprecation")
public class CreatePlaceFragment extends Fragment {
    private View view;
    private ImageView imgPlace;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtDescription;
    private CreatePlaceViewModel createPlaceViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        imgPlace = view.findViewById(R.id.imgPlace);
        imgPlace.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlaceViewModel.setName(editable.toString());
            }
        });

        txtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlaceViewModel.setAddress(editable.toString());
            }
        });

        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlaceViewModel.setDescription(editable.toString());
            }
        });

        Button btnCreatePlace = view.findViewById(R.id.btnCreatePlace);
        btnCreatePlace.setOnClickListener(OnClickListener -> {
            boolean errors = false;

            if(!createPlaceViewModel.isNameCorrect(createPlaceViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_place_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            if(!createPlaceViewModel.isAddressCorrect(createPlaceViewModel.getAddress())) {
                txtAddress.setError(getResources().getString(R.string.invalid_place_address));
                errors = true;
            } else {
                txtAddress.setError(null);
            }

            if(!createPlaceViewModel.isDescriptionCorrect(createPlaceViewModel.getDescription())) {
                txtDescription.setError(getResources().getString(R.string.invalid_place_description));
                errors = true;
            } else {
                txtDescription.setError(null);
            }

            if(!errors) {
                if(createPlaceViewModel.isImageUploaded(createPlaceViewModel.getImage())) {
                    // TODO: Insert place API
                } else {
                    new ErrorDialog(getString(R.string.error_dialog_title), getString(R.string.pick_place_image), "PLACE_IMAGE_ERROR").show(getChildFragmentManager(), ErrorDialog.TAG);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgPlace.setImageURI(selectedImage);
            createPlaceViewModel.setImage(selectedImage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_place, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.createPlaceToolbar);
        toolbar.setTitle(R.string.create_place_screen_title);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_right_bottom_bold);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        createPlaceViewModel = new ViewModelProvider(this).get(CreatePlaceViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtDescription = view.findViewById(R.id.txtDescription);

        if(createPlaceViewModel.getImage() != null)
            imgPlace.setImageURI(createPlaceViewModel.getImage());

        if(!createPlaceViewModel.getName().equals(""))
            txtName.setText(createPlaceViewModel.getName());

        if(!createPlaceViewModel.getAddress().equals(""))
            txtName.setText(createPlaceViewModel.getAddress());

        if(!createPlaceViewModel.getDescription().equals(""))
            txtName.setText(createPlaceViewModel.getDescription());
    }

}