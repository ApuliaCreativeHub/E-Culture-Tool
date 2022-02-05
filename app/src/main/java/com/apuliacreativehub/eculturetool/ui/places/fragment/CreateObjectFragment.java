package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.component.Utils;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CreateObjectViewModel;

@SuppressWarnings("deprecation")
public class CreateObjectFragment extends Fragment {
    private View view;
    private final Bundle bundleZoneNameId;
    private final ArrayAdapter<String> listZones;
    private ImageView imgObject;
    private EditText txtName;
    private AutoCompleteTextView txtRoom;
    private ArrayAdapter arrayOptionsAdapter;
    private EditText txtDescription;
    private CreateObjectViewModel createObjectViewModel;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            takeImgFromGallery();
        } else {
            takeStandardImg();
        }
    });

    final Observer<RepositoryNotification<Object>> addObjectObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        view.findViewById(R.id.createObjectProgressBar).setVisibility(View.GONE);
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage()==null || notification.getErrorMessage().isEmpty()) {
                Log.i("addObject", "OK");
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Log.i("addObject", "Not OK");
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.ADD_OBJECTS_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.i("addObject", "Not OK (exception)");
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.ADD_OBJECT_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
        }
    };

    public CreateObjectFragment(Bundle zoneNameID, ArrayAdapter<String> listZones){
        this.bundleZoneNameId = zoneNameID;
        this.listZones = listZones;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_object, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.createObjectToolbar);
        toolbar.setTitle(R.string.create_object_title_screen);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        arrayOptionsAdapter = listZones;
        txtRoom = view.findViewById(R.id.txtRoom);
        txtRoom.setAdapter(arrayOptionsAdapter);

        createObjectViewModel = new ViewModelProvider(this).get(CreateObjectViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtDescription = view.findViewById(R.id.txtDescription);

        if(createObjectViewModel.getImage() != null)
            imgObject.setImageURI(createObjectViewModel.getImage());

        if(!createObjectViewModel.getName().equals(""))
            txtName.setText(createObjectViewModel.getName());

        if(!createObjectViewModel.getZone().equals(""))
            txtRoom.setText(createObjectViewModel.getZone());

        if(!createObjectViewModel.getDescription().equals(""))
            txtDescription.setText(createObjectViewModel.getDescription());
    }

    public void onStart() {
        super.onStart();

        imgObject = view.findViewById(R.id.imgObject);
        imgObject.setOnClickListener(view -> requestPermission());

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createObjectViewModel.setName(editable.toString());
            }
        });

        txtRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createObjectViewModel.setZone(editable.toString());
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
                createObjectViewModel.setDescription(editable.toString());
            }
        });

        Button btnCreateObject = view.findViewById(R.id.btnCreateObject);
        btnCreateObject.setOnClickListener(v -> {
            boolean errors = false;

            if (!createObjectViewModel.isNameCorrect(createObjectViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_place_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            if (!createObjectViewModel.isRoomSelected(createObjectViewModel.getZone())) {
                txtRoom.setError(getResources().getString(R.string.zone_not_selected));
                errors = true;
            } else {
                txtRoom.setError(null);
            }

            if(!createObjectViewModel.isDescriptionCorrect(createObjectViewModel.getDescription())) {
                txtDescription.setError(getResources().getString(R.string.invalid_place_description));
                errors = true;
            } else {
                txtDescription.setError(null);
            }

            if(!errors) {
                if(createObjectViewModel.isImageUploaded(createObjectViewModel.getImage())) {
                    view.findViewById(R.id.createObjectProgressBar).setVisibility(View.VISIBLE);
                    createObjectViewModel.addObject().observe(this, addObjectObserver);
                } else {
                    new Dialog(getString(R.string.error_dialog_title), getString(R.string.pick_object_image), DialogTags.PLACE_IMAGE_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            }
        });

        txtRoom.setOnItemClickListener((parent, view, position, id) -> {
            if (txtRoom.getInputType() != EditorInfo.TYPE_NULL)
                txtRoom.setInputType(EditorInfo.TYPE_NULL);

            if (txtRoom.getError() != null)
                txtRoom.setError(null);

            createObjectViewModel.setZoneID(bundleZoneNameId.getInt((String) ((TextView) view.findViewById(R.id.zoneName)).getText(), 0));
        });
    }

    private void takeImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgObject.setImageURI(selectedImage);
            createObjectViewModel.setImage(selectedImage);
        }
    }

    private void takeStandardImg(){
        imgObject.setImageResource(R.drawable.object);
        createObjectViewModel.setImage(Uri.parse(Utils.DRAWABLE_URI_BASE_PATH + "object"));
    }

    private void showRationaleDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(R.string.rationale_read_external_storage_message)
                .setPositiveButton(R.string.rationale_ok_button, (dialog, id) -> requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE)).setNegativeButton(R.string.rationale_cancel_button, (dialog, id) -> takeStandardImg());
        builder.create().show();
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

}