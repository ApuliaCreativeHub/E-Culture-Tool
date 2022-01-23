package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.ErrorDialog;

@SuppressWarnings("deprecation")
public class EditPlaceFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private View view;
    private ImageView imgPlace;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtDescription;
    private EditPlaceViewModel editPlaceViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_place, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.editPlaceToolbar);
        toolbar.setTitle(R.string.edit_place_screen_title);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        editPlaceViewModel = new ViewModelProvider(this).get(EditPlaceViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtDescription = view.findViewById(R.id.txtDescription);

        if(editPlaceViewModel.getImage() != null)
            imgPlace.setImageURI(editPlaceViewModel.getImage());

        if(!editPlaceViewModel.getName().equals(""))
            txtName.setText(editPlaceViewModel.getName());

        if(!editPlaceViewModel.getAddress().equals(""))
            txtName.setText(editPlaceViewModel.getAddress());

        if(!editPlaceViewModel.getDescription().equals(""))
            txtName.setText(editPlaceViewModel.getDescription());
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
                editPlaceViewModel.setName(editable.toString());
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
                editPlaceViewModel.setAddress(editable.toString());
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
                editPlaceViewModel.setDescription(editable.toString());
            }
        });

        Button btnChangePlaceInformation = view.findViewById(R.id.btnChangePlaceInformation);
        btnChangePlaceInformation.setOnClickListener(OnClickListener -> {
            boolean errors = false;

            if(!editPlaceViewModel.isNameCorrect(editPlaceViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_place_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            if(!editPlaceViewModel.isAddressCorrect(editPlaceViewModel.getAddress())) {
                txtAddress.setError(getResources().getString(R.string.invalid_place_address));
                errors = true;
            } else {
                txtAddress.setError(null);
            }

            if(!editPlaceViewModel.isDescriptionCorrect(editPlaceViewModel.getDescription())) {
                txtDescription.setError(getResources().getString(R.string.invalid_place_description));
                errors = true;
            } else {
                txtDescription.setError(null);
            }

            if(!errors) {
                if(editPlaceViewModel.isImageUploaded(editPlaceViewModel.getImage())) {
                    // TODO: Update place API
                } else {
                    new ErrorDialog(getString(R.string.error_dialog_title), getString(R.string.pick_place_image), "PLACE_IMAGE_ERROR").show(getChildFragmentManager(), ErrorDialog.TAG);
                }
            }
        });

        Button btnDeletePlace = view.findViewById(R.id.btnDeletePlace);
        btnDeletePlace.setOnClickListener(delete -> showNoticeDialog());
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_place), "DELETE_PLACE");
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        // TODO: Delete place API
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgPlace.setImageURI(selectedImage);
            editPlaceViewModel.setImage(selectedImage);
        }
    }

}