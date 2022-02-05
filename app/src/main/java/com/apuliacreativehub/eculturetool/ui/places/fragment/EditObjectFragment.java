package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.component.QRCodeHelper;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.EditObjectViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("deprecation")
public class EditObjectFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private final String initialSelectedZone;
    private View view;
    private ImageView imgObject;
    private EditText txtName;
    private final Object object;
    private final Bundle bundleZoneNameId;
    private final ArrayAdapter<String> listZones;
    private AutoCompleteTextView txtRoom;
    private ArrayAdapter arrayOptionsAdapter;
    private ArrayList<String> roomsDataset;
    private EditText txtDescription;
    private EditObjectViewModel editObjectViewModel;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            takeImgFromGallery();
        } else {
            takeStandardImg();
        }
    });

    final Observer<RepositoryNotification<Object>> editObjectObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        view.findViewById(R.id.editObjectProgressBar).setVisibility(View.GONE);
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage()==null || notification.getErrorMessage().isEmpty()) {
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

    final Observer<RepositoryNotification<Void>> deleteObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        view.findViewById(R.id.editObjectProgressBar).setVisibility(View.GONE);
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage()==null || notification.getErrorMessage().isEmpty()) {
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

    public EditObjectFragment(Object object, Bundle zoneNameID, ArrayAdapter<String> listZones, String selectedZone){
        this.bundleZoneNameId = zoneNameID;
        this.listZones = listZones;
        this.object = object;
        this.initialSelectedZone = selectedZone;
    }

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

        arrayOptionsAdapter = listZones;
        txtRoom = view.findViewById(R.id.txtRoom);
        txtRoom.setAdapter(arrayOptionsAdapter);

        editObjectViewModel = new ViewModelProvider(this).get(EditObjectViewModel.class);
        editObjectViewModel.setObjectID(object.getId());

        txtName = view.findViewById(R.id.txtName);
        txtDescription = view.findViewById(R.id.txtDescription);
        imgObject = view.findViewById(R.id.imgObject);
        txtRoom.setText(initialSelectedZone, false);
        txtName.setText(object.getName());
        txtDescription.setText(object.getDescription());
        Glide.with(requireContext()).asBitmap()
                .load("https://hiddenfile.ml/ecultureapi/" + object.getNormalSizeImg())
                //.load("http://10.0.2.2:8080/" + object.getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imgObject);

        if(editObjectViewModel.getImage() != null)
            imgObject.setImageURI(editObjectViewModel.getImage());

        if(!editObjectViewModel.getName().equals(""))
            txtName.setText(editObjectViewModel.getName());
        else
            editObjectViewModel.setName(txtName.getText().toString());

        if(!editObjectViewModel.getZone().equals(""))
            txtRoom.setText(editObjectViewModel.getZone());
        else{
            editObjectViewModel.setZoneID(bundleZoneNameId.getInt(txtRoom.getText().toString()));
            editObjectViewModel.setZone(txtRoom.getText().toString());
        }

        if(!editObjectViewModel.getDescription().equals(""))
            txtDescription.setText(editObjectViewModel.getDescription());
        else
            editObjectViewModel.setDescription(txtDescription.getText().toString());
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
                editObjectViewModel.setZone(editable.toString());
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
        btnChangeObjectInformation.setOnClickListener(v -> {
            boolean errors = false;

            if (!editObjectViewModel.isNameCorrect(editObjectViewModel.getName())) {
                txtName.setError(getResources().getString(R.string.invalid_place_name));
                errors = true;
            } else {
                txtName.setError(null);
            }

            if (!editObjectViewModel.isRoomSelected(editObjectViewModel.getZone())) {
                txtRoom.setError(getResources().getString(R.string.zone_not_selected));
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
                    try {
                        view.findViewById(R.id.editObjectProgressBar).setVisibility(View.VISIBLE);
                        editObjectViewModel.editObject().observe(this, editObjectObserver);
                    } catch (NoInternetConnectionException e) {
                        view.findViewById(R.id.editObjectProgressBar).setVisibility(View.GONE);
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                    }
            }
        });

        Button btnDeleteObject = view.findViewById(R.id.btnDeleteObject);
        btnDeleteObject.setOnClickListener(view -> showNoticeDialog());

        TextView btnDownloadQRCode = view.findViewById(R.id.btnDownloadQRCode);
        btnDownloadQRCode.setOnClickListener(view -> {
           downloadQRCode(String.valueOf(object.getId()));
        });

        txtRoom.setOnItemClickListener((parent, view, position, id) -> {
            if (txtRoom.getInputType() != EditorInfo.TYPE_NULL)
                txtRoom.setInputType(EditorInfo.TYPE_NULL);

            if (txtRoom.getError() != null)
                txtRoom.setError(null);

            editObjectViewModel.setZoneID(bundleZoneNameId.getInt((String) ((TextView) view.findViewById(R.id.zoneName)).getText(), 0));
        });
    }

    private void downloadQRCode(String objectID) {
        ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if(!file.exists() && !file.mkdirs()) {
            file.mkdirs();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "IMG" + date + ".png";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);

        try {
            Bitmap QRCode = QRCodeHelper.generateQRCode(objectID);
            fileOutputStream = new FileOutputStream(new_file);
            QRCode.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.qrcode_download_success), Toast.LENGTH_SHORT).show();
        } catch(WriterException | IOException exception) {
            Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.qrcode_download_error), Toast.LENGTH_SHORT).show();
        }

        refreshGallery(new_file);
    }

    private void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        requireActivity().getApplicationContext().sendBroadcast(intent);
    }

    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(file, "Download");
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_object), DialogTags.DELETE_OBJECTS_WARNING);
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        try {
            view.findViewById(R.id.editObjectProgressBar).setVisibility(View.VISIBLE);
            editObjectViewModel.deleteObject().observe(this, deleteObserver);
        } catch (NoInternetConnectionException e) {
            view.findViewById(R.id.editObjectProgressBar).setVisibility(View.GONE);
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
        }
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