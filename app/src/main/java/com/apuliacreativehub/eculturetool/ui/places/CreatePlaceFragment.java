package com.apuliacreativehub.eculturetool.ui.places;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

@SuppressWarnings("deprecation")
public class CreatePlaceFragment extends Fragment {
    private View view;
    private ImageView imgPlace;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtDescription;
    private CreatePlaceViewModel createPlaceViewModel;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            takeImgFromGallery();
        }else {
            takeStandardImg();
        }
    });

    final Observer<RepositoryNotification<Place>> addPlaceObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null || notification.getErrorMessage().isEmpty()) {
                Log.i("addPlace", "OK");
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Log.i("addPlace", "Not OK");
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "UPDATING_PROFILE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.i("addPlace", "Not OK exception");
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "UPDATING_PROFILE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        imgPlace = view.findViewById(R.id.imgPlace);
        imgPlace.setOnClickListener(view -> requestPermission());

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
                    createPlaceViewModel.addPlace().observe(this, addPlaceObserver);
                } else {
                    new Dialog(getString(R.string.error_dialog_title), getString(R.string.pick_place_image), "PLACE_IMAGE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            }
        });
    }

    private void takeImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    private void takeStandardImg(){
        imgPlace.setImageResource(R.drawable.museum);
        createPlaceViewModel.setImage(Uri.parse(Utils.DRAWABLE_URI_BASE_PATH + "museum"));
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(
                requireContext()
                , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takeImgFromGallery();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showRationaleDialog(getString(R.string.rationale_read_external_storage_title));
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
            );
        }
    }

    private void showRationaleDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(R.string.rationale_read_external_storage_message)
                .setPositiveButton(R.string.rationale_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissionLauncher.launch(
                                Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).setNegativeButton(R.string.rationale_cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                takeStandardImg();
            }
        });
        builder.create().show();
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

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
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
            txtAddress.setText(createPlaceViewModel.getAddress());

        if(!createPlaceViewModel.getDescription().equals(""))
            txtDescription.setText(createPlaceViewModel.getDescription());
    }

}