package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Intent;
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
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.QRCodeHelper;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
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
    private ListArtifactsManageAdapter listArtifactsManageAdapter;
    private ArrayList<String> mDataset;
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
                    arrayOptionsAdapter.clear();
                    for (Zone zone : notification.getData()) {
                        managePlaceViewModel.getZoneNames().add(zone.getName());
                    }
                    arrayOptionsAdapter.addAll(managePlaceViewModel.getZoneNames());
                    autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                    arrayOptionsAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "GET_ZONES_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "GET_ZONES_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Zone>> addZonesObserver = new Observer<RepositoryNotification<Zone>>() {
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
                    arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_select_room, managePlaceViewModel.getZoneNames());
                    autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                    arrayOptionsAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Dialog", "show dialog here");
                    // TODO: Fix dialog tag
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "GET_ZONES_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                // TODO: Fix dialog tag
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "GET_ZONES_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Zone>> updateObserver = new Observer<RepositoryNotification<Zone>>() {
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
                        arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_select_room, managePlaceViewModel.getZoneNames());
                        autoCompleteTextView.setAdapter(arrayOptionsAdapter);
                        arrayOptionsAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "UPDATE_ZONE_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "UPDATE_ZONE_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
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

        arrayOptionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_select_room);

        // TODO: Read Object API
        mDataset = new ArrayList<>();
        mDataset.add("CIAO");
        mDataset.add("CIAO1");
        mDataset.add("CIAO2");
        mDataset.add("CIAO3");
        mDataset.add("CIAO4");

        return inflater.inflate(R.layout.fragment_manage_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        setSelectElement();
        setDynamicRecycleView();
        Toolbar toolbar = view.findViewById(R.id.managePlaceToolbar);
        toolbar.setTitle(R.string.manage_place_screen_title);
        toolbar.inflateMenu(R.menu.top_menu_manage_place);
        toolbar.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.editPlaceInformation:
                    TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new EditPlaceFragment(place));
                    break;
                case R.id.editArtifactByQrCode:
                    // Initialize intent integrator
                    IntentIntegrator intentIntegrator = new IntentIntegrator(
                            requireActivity()
                    );
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
            // TODO: Transaction to EditObjectFragment with putExtra(intentResult.getContents())
        } else {
            // When result content is null
            // Display toast
            Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.scan_qrcode_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSelectElement() {
        managePlaceViewModel.getZonesFromDatabase().observe(getViewLifecycleOwner(), getZonesObserver);
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(arrayOptionsAdapter);
    }

    private void setDynamicRecycleView() {
        recyclerGridView = view.findViewById(R.id.recyclerContainerObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerGridView.setLayoutManager(gridLayoutManager);
        listArtifactsManageAdapter = new ListArtifactsManageAdapter(R.layout.component_card_artifact, mDataset, getContext());
        recyclerGridView.setAdapter(listArtifactsManageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if(autoCompleteTextView.getInputType() != EditorInfo.TYPE_NULL)
                autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);

            if(autoCompleteTextView.getError() != null)
                autoCompleteTextView.setError(null);

            managePlaceViewModel.setCurrentlySelectedZoneName((String) ((TextView) view.findViewById(R.id.zoneName)).getText());
            selected = true;
            roomId = position;
        });

        autoCompleteTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            String name = textView.getText().toString();
            if(checkRoomName(name)) {
                autoCompleteTextView.setError(null);
                if(add) {
                    try {
                        managePlaceViewModel.addZonesToDatabase(name).observe(this, addZonesObserver);
                    } catch (NoInternetConnectionException e) {
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), "NO_INTERNET_CONNECTION_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                    }
                } else {
                    // TODO: Update zone: create an observer for zone update and update zone in zones list in ManageViewModel on success
                    try {
                        managePlaceViewModel.editZoneOnDatabase(name).observe(this, updateObserver);
                    } catch (NoInternetConnectionException e) {
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), "NO_INTERNET_CONNECTION_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                    }
                }
            } else {
                autoCompleteTextView.setError(getString(R.string.invalid_room));
            }

            autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);
            autoCompleteTextView.setText("");

            return true;
        });

        Button btnRoomOptions = view.findViewById(R.id.btnRoomOptions);
        btnRoomOptions.setOnClickListener(view -> showMenu(view, R.menu.context_menu_room));

        FloatingActionButton btnCreateObject = view.findViewById(R.id.btnCreateObject);
        btnCreateObject.setOnClickListener(view -> TransactionHelper.transactionWithAddToBackStack(requireActivity(), R.id.fragment_container_layout, new CreateObjectFragment()));
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
        DialogFragment dialog = new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_delete_room), "DELETE_ROOM");
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");

        if(selected) {
            //roomsDataset.remove(roomId);

            arrayOptionsAdapter.clear();
            //arrayOptionsAdapter.addAll(roomsDataset);
            arrayOptionsAdapter.notifyDataSetChanged();

            autoCompleteTextView.setText("");

            selected = false;
            // TODO: Delete zone: create an observer for zone deletion and remove zone from zones list in ManageViewModel on success
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

}