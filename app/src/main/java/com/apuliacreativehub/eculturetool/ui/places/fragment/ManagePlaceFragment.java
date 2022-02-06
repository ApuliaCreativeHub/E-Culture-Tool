package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.component.QRCodeHelper;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.component.Utils;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListObjectsManageAdapter;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.ManagePlaceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class ManagePlaceFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private static final int NUMBER_COLUMN = 2;
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;

    private View view;
    private ArrayAdapter<String> arrayOptionsAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerGridView;
    private GridLayoutManager gridLayoutManager;
    private ListObjectsManageAdapter listObjectsManageAdapter;
    private ArrayList<Object> mDataset;
    private boolean selected = false;
    private boolean add;
    private int roomId;
    private final Place place;
    private ManagePlaceViewModel managePlaceViewModel;
    final Observer<RepositoryNotification<ArrayList<Zone>>> getZonesObserver = new Observer<RepositoryNotification<ArrayList<Zone>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Zone>> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    managePlaceViewModel.setZones(notification.getData());
                    managePlaceViewModel.getZoneNames().clear();
                    arrayOptionsAdapter.clear();
                    for (Zone zone : notification.getData()) {
                        managePlaceViewModel.getZoneNames().add(zone.getName());
                    }
                    arrayOptionsAdapter.addAll(managePlaceViewModel.getZoneNames());
                    autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                    arrayOptionsAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.GET_ZONES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.GET_ZONES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<ArrayList<Object>>> getObjectObserver = new Observer<RepositoryNotification<ArrayList<Object>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Object>> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    mDataset = notification.getData();
                    setDynamicRecycleView();
                    view.findViewById(R.id.objectsProgressBar).setVisibility(View.GONE);
                } else {
                    Log.d("Dialog", "show dialog here");
                    view.findViewById(R.id.objectsProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.GET_OBJECTS_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                view.findViewById(R.id.objectsProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.GET_OBJECTS_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Zone>> addZoneObserver = new Observer<RepositoryNotification<Zone>>() {
        @Override
        public void onChanged(RepositoryNotification<Zone> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    managePlaceViewModel.addZone(notification.getData());
                    managePlaceViewModel.getZoneNames().add(notification.getData().getName());
                    arrayOptionsAdapter.clear();
                    arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.component_item_select_room, managePlaceViewModel.getZoneNames());
                    autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                    arrayOptionsAdapter.notifyDataSetChanged();
                    view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                } else {
                    Log.d("Dialog", "show dialog here");
                    view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.ADD_ZONES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.ADD_ZONES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Zone>> updateZoneObserver = new Observer<RepositoryNotification<Zone>>() {
        @Override
        public void onChanged(RepositoryNotification<Zone> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    Zone editedZone = managePlaceViewModel.getZoneById(notification.getData().getId());
                    if (editedZone != null) {
                        int i = managePlaceViewModel.getZones().indexOf(editedZone);
                        managePlaceViewModel.getZones().set(i, notification.getData());
                        managePlaceViewModel.getZoneNames().set(i, notification.getData().getName());
                        arrayOptionsAdapter.clear();
                        arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.component_item_select_room, managePlaceViewModel.getZoneNames());
                        autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                        arrayOptionsAdapter.notifyDataSetChanged();
                        view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                    }
                } else {
                    Log.d("Dialog", "show dialog here");
                    view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.UPDATE_ZONES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.UPDATE_ZONES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Zone>> deleteZoneObserver = new Observer<RepositoryNotification<Zone>>() {
        @Override
        public void onChanged(RepositoryNotification<Zone> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    managePlaceViewModel.removeZoneById(notification.getData().getId());
                    managePlaceViewModel.getZoneNames().remove(notification.getData().getName());
                    arrayOptionsAdapter.clear();
                    arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.component_item_select_room, managePlaceViewModel.getZoneNames());
                    autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                    arrayOptionsAdapter.notifyDataSetChanged();
                    mDataset.clear();
                    listObjectsManageAdapter.notifyDataSetChanged();
                    view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                } else {
                    Log.d("Dialog", "show dialog here");
                    view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.DELETE_ZONES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.DELETE_ZONES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Object>> getObjectFromQR = new Observer<RepositoryNotification<Object>>() {
        @Override
        public void onChanged(RepositoryNotification<Object> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            view.findViewById(R.id.objectsProgressBar).setVisibility(View.GONE);
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new EditObjectFragment(notification.getData(), managePlaceViewModel.getZonesBundle(), arrayOptionsAdapter, managePlaceViewModel.getZoneById(notification.getData().getZoneId()).getName()));
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.GET_OBJECTS_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.GET_OBJECTS_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    public ManagePlaceFragment(Place place) {
        super();
        this.place = place;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        managePlaceViewModel = new ViewModelProvider(this).get(ManagePlaceViewModel.class);
        managePlaceViewModel.setPlace(place);

        arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.component_item_select_room);

        return inflater.inflate(R.layout.fragment_manage_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        Toolbar toolbar = view.findViewById(R.id.managePlaceToolbar);
        toolbar.setTitle(R.string.manage_place_screen_title);
        toolbar.inflateMenu(R.menu.top_menu_manage_place);
        toolbar.setOnMenuItemClickListener(item -> {
            if (Utils.checkConnection((ConnectivityManager) requireActivity().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                switch(item.getItemId()) {
                    case R.id.editPlaceInformation:
                        TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new EditPlaceFragment(place));
                        break;
                    case R.id.editArtifactByQrCode:
                        // Initialize intent integrator
                        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(ManagePlaceFragment.this);
                        // Set prompt text
                        intentIntegrator.setPrompt(getString(R.string.scan_qrcode_prompt));
                        // Set beep
                        intentIntegrator.setBeepEnabled(true);
                        // Locked orientation
                        intentIntegrator.setOrientationLocked(true);
                        // Set capture activity
                        intentIntegrator.setCaptureActivity(QRCodeHelper.class);
                        // Initialize scan
                        intentIntegrator.initiateScan();
                        break;
                }
            }else{
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
            return true;
        });

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        // Check condition
        if(intentResult.getContents() != null) {
            // When result content is not null
            // Initialize alert dialog
            Log.i("Object ID", intentResult.getContents());
            try {
                view.findViewById(R.id.objectsProgressBar).setVisibility(View.VISIBLE);
                managePlaceViewModel.getObjectById(Integer.parseInt(intentResult.getContents())).observe(this, getObjectFromQR);
            } catch (NoInternetConnectionException e) {
                view.findViewById(R.id.objectsProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }

        } else {
            // When result content is null
            // Display toast
            Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.scan_qrcode_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSelectElement() {
        managePlaceViewModel.getZonesFromDatabase().observe(getViewLifecycleOwner(), getZonesObserver);
        autoCompleteTextView = view.findViewById(R.id.selectZonesAutoComplete);
        autoCompleteTextView.setAdapter(arrayOptionsAdapter);
    }

    private void setDynamicRecycleView() {
        recyclerGridView = view.findViewById(R.id.recyclerContainerObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerGridView.setLayoutManager(gridLayoutManager);
        listObjectsManageAdapter = new ListObjectsManageAdapter(R.layout.component_card_artifact, mDataset, getContext(), arrayOptionsAdapter, managePlaceViewModel.getZonesBundle(), managePlaceViewModel.getCurrentlySelectedZoneName());
        recyclerGridView.setAdapter(listObjectsManageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        setSelectElement();

        if(!managePlaceViewModel.getCurrentlySelectedZoneName().equals("")){
            view.findViewById(R.id.objectsProgressBar).setVisibility(View.VISIBLE);
            managePlaceViewModel.getObjectByZone().observe(getViewLifecycleOwner(), getObjectObserver);
        }

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if (autoCompleteTextView.getInputType() != EditorInfo.TYPE_NULL)
                autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);

            if (autoCompleteTextView.getError() != null)
                autoCompleteTextView.setError(null);

            managePlaceViewModel.setCurrentlySelectedZoneName((String) ((TextView) view.findViewById(R.id.zoneName)).getText());
            selected = true;
            roomId = position;

            managePlaceViewModel.getObjectByZone().observe(getViewLifecycleOwner(), getObjectObserver);
        });

        autoCompleteTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            String name = textView.getText().toString();
            if(checkRoomName(name)) {
                autoCompleteTextView.setError(null);
                if(add) {
                    try {
                        view.findViewById(R.id.zoneProgressBar).setVisibility(View.VISIBLE);
                        managePlaceViewModel.addZoneToDatabase(name).observe(this, addZoneObserver);
                    } catch (NoInternetConnectionException e) {
                        view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                    }
                } else {
                    try {
                        view.findViewById(R.id.zoneProgressBar).setVisibility(View.VISIBLE);
                        managePlaceViewModel.editZoneOnDatabase(name).observe(this, updateZoneObserver);
                    } catch (NoInternetConnectionException e) {
                        view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                    }
                }
            } else {
                autoCompleteTextView.setError(getString(R.string.invalid_zone));
            }

            autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);
            autoCompleteTextView.setText("");

            return true;
        });

        Button btnRoomOptions = view.findViewById(R.id.btnZoneOptions);
        btnRoomOptions.setOnClickListener(view -> showMenu(view, R.menu.context_menu_room));

        FloatingActionButton btnCreateObject = view.findViewById(R.id.btnCreateObject);
        btnCreateObject.setOnClickListener(view -> {
                    if (Utils.checkConnection((ConnectivityManager) requireActivity().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        TransactionHelper.transactionWithAddToBackStack(requireActivity(),
                                R.id.fragment_container_layout,
                                new CreateObjectFragment(managePlaceViewModel.getZonesBundle(), arrayOptionsAdapter));
                    } else {
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                    }
                }
        );
    }

    private boolean checkRoomName(String name) {
        return name.length() >= MIN_LENGTH_NAME && name.length() <= MAX_LENGTH_NAME;
    }

    private void showMenu(View view, int menu) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.addRoom:
                    add = true;
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                    break;
                case R.id.editRoom:
                    if(selected) {
                        add = false;
                        selected = false;
                        autoCompleteTextView.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                    }
                    break;
                case R.id.deleteRoom:
                    showNoticeDialog();
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_zone), DialogTags.DELETE_ZONES_WARNING);
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");

        if(selected) {
            autoCompleteTextView.setText("");
            selected = false;
            try {
                view.findViewById(R.id.zoneProgressBar).setVisibility(View.VISIBLE);
                managePlaceViewModel.deleteZoneFromDatabase(managePlaceViewModel.getCurrentlySelectedZoneName()).observe(this, deleteZoneObserver);
            } catch (NoInternetConnectionException e) {
                view.findViewById(R.id.zoneProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

}