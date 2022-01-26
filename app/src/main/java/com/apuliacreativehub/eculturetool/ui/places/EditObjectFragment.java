package com.apuliacreativehub.eculturetool.ui.places;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class EditObjectFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private View view;
    private ImageView imgObject;
    private EditText txtName;
    private AutoCompleteTextView txtRoom;
    private ArrayAdapter arrayOptionsAdapter;
    private ArrayList<String> roomsDataset;
    private EditText txtDescription;
    private EditObjectViewModel editObjectViewModel;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            takeImgFromGallery();
        }else {
            takeStandardImg();
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_object, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.editObjectToolbar);
        toolbar.setTitle(R.string.edit_object_screen_title);

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        //TODO: Read Room API
        roomsDataset = new ArrayList<>();
        roomsDataset.add("Stanza A");
        roomsDataset.add("Stanza B");
        roomsDataset.add("Stanza C");
        arrayOptionsAdapter = new ArrayAdapter(getContext(), R.layout.item_select_room, roomsDataset);
        txtRoom = view.findViewById(R.id.txtRoom);
        txtRoom.setAdapter(arrayOptionsAdapter);

        editObjectViewModel = new ViewModelProvider(this).get(EditObjectViewModel.class);

        txtName = view.findViewById(R.id.txtName);
        txtDescription = view.findViewById(R.id.txtDescription);

        if(editObjectViewModel.getImage() != null)
            imgObject.setImageURI(editObjectViewModel.getImage());

        if(!editObjectViewModel.getName().equals(""))
            txtName.setText(editObjectViewModel.getName());

        if(!editObjectViewModel.getRoom().equals(""))
            txtRoom.setText(editObjectViewModel.getRoom());

        if(!editObjectViewModel.getDescription().equals(""))
            txtDescription.setText(editObjectViewModel.getDescription());
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
                editObjectViewModel.setName(editable.toString());
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
                editObjectViewModel.setRoom(editable.toString());
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
                editObjectViewModel.setDescription(editable.toString());
            }
        });

        Button btnChangeObjectInformation = view.findViewById(R.id.btnChangeObjectInformation);
        btnChangeObjectInformation.setOnClickListener(view -> {
            boolean errors = false;

            if(!editObjectViewModel.isNameCorrect(editObjectViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_place_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            if(!editObjectViewModel.isRoomSelected(editObjectViewModel.getRoom())) {
                txtRoom.setError(getResources().getString(R.string.room_not_selected));
                errors = true;
            } else {
                txtRoom.setError(null);
            }

            if(!editObjectViewModel.isDescriptionCorrect(editObjectViewModel.getDescription())) {
                txtDescription.setError(getResources().getString(R.string.invalid_place_description));
                errors = true;
            } else {
                txtDescription.setError(null);
            }

            if(!errors) {
                if(editObjectViewModel.isImageUploaded(editObjectViewModel.getImage())) {
                    // TODO: Update Object API
                } else {
                    new Dialog(getString(R.string.error_dialog_title), getString(R.string.pick_object_image), "PLACE_IMAGE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            }
        });

        Button btnDeleteObject = view.findViewById(R.id.btnDeleteObject);
        btnDeleteObject.setOnClickListener(view -> showNoticeDialog());

        Button btnDownloadQRCode = view.findViewById(R.id.btnDownloadQRCode);
        // TODO: Intent Download Image (QR Code)
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_object), "DELETE_OBJECT");
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        // TODO: Delete Object API
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
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
            editObjectViewModel.setImage(selectedImage);
        }
    }

    private void takeStandardImg(){
        imgObject.setImageResource(R.drawable.object);
        editObjectViewModel.setImage(Uri.parse(String.valueOf(R.drawable.object)));
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