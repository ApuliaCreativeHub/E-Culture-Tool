package com.apuliacreativehub.eculturetool.ui.places.fragment;

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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.BuildConfig;
import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.component.Utils;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.EditPlaceViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

@SuppressWarnings("deprecation")
public class EditPlaceFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private View view;
    private ImageView imgPlace;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtDescription;
    private EditPlaceViewModel editPlaceViewModel;
    private final Place place;
    final Observer<RepositoryNotification<Place>> editPlaceObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        view.findViewById(R.id.editPlaceProgressBar).setVisibility(View.GONE);
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null || notification.getErrorMessage().isEmpty()) {
                Log.i("addPlace", "OK");
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Log.i("addPlace", "Not OK");
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.UPDATE_PROFILE_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.i("addPlace", "Not OK exception");
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.UPDATE_PROFILE_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
        }
    };
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            takeImgFromGallery();
        } else {
            takeStandardImg();
        }
    });
    private final Observer<RepositoryNotification<Void>> deletePlaceObserver = new Observer<RepositoryNotification<Void>>() {
        @Override
        public void onChanged(RepositoryNotification<Void> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            view.findViewById(R.id.editPlaceProgressBar).setVisibility(View.GONE);
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    requireActivity().finish();
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.DELETE_PLACES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.DELETE_PLACES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    public EditPlaceFragment(Place place) {
        this.place = place;
    }

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
        editPlaceViewModel.setId(place.getId());

        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtDescription = view.findViewById(R.id.txtDescription);
        imgPlace = view.findViewById(R.id.imgPlace);
        txtName.setText(place.getName());
        txtAddress.setText(place.getAddress());
        txtDescription.setText(place.getDescription());
        Glide.with(requireContext()).asBitmap()
                .load(BuildConfig.API_URL + place.getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imgPlace);

        if(editPlaceViewModel.getImage() != null)
            imgPlace.setImageURI(editPlaceViewModel.getImage());

        if(!editPlaceViewModel.getName().equals(""))
            txtName.setText(editPlaceViewModel.getName());
        else
            editPlaceViewModel.setName(txtName.getText().toString());

        if(!editPlaceViewModel.getAddress().equals(""))
            txtName.setText(editPlaceViewModel.getAddress());
        else
            editPlaceViewModel.setAddress(txtAddress.getText().toString());

        if(!editPlaceViewModel.getDescription().equals(""))
            txtDescription.setText(editPlaceViewModel.getDescription());
        else
            editPlaceViewModel.setDescription(txtDescription.getText().toString());
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
                try {
                    view.findViewById(R.id.editPlaceProgressBar).setVisibility(View.VISIBLE);
                    editPlaceViewModel.editPlace().observe(this, editPlaceObserver);
                } catch (NoInternetConnectionException e) {
                    view.findViewById(R.id.editPlaceProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }

            }
        });

        imgPlace.setOnClickListener(view -> requestPermission());

        Button btnDeletePlace = view.findViewById(R.id.btnDeletePlace);
        btnDeletePlace.setOnClickListener(delete -> showNoticeDialog());
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_place), DialogTags.DELETE_PLACES_WARNING);
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        try {
            view.findViewById(R.id.editPlaceProgressBar).setVisibility(View.VISIBLE);
            editPlaceViewModel.deletePlace().observe(this, deletePlaceObserver);
        } catch (NoInternetConnectionException e) {
            view.findViewById(R.id.editPlaceProgressBar).setVisibility(View.GONE);
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
        }
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
            place.setUriImg(selectedImage.getPath());
            editPlaceViewModel.setImage(selectedImage);
        }
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

    private void takeImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
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

    private void takeStandardImg(){
        imgPlace.setImageResource(R.drawable.museum);
        editPlaceViewModel.setImage(Uri.parse(Utils.DRAWABLE_URI_BASE_PATH + "museum"));
    }
}