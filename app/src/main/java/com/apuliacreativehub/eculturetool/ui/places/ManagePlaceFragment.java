package com.apuliacreativehub.eculturetool.ui.places;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.ListArtifactsAdapter;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManagePlaceFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private static final int NUMBER_COLUMN = 2;
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;

    private View view;
    private ArrayAdapter arrayOptionsAdapter;
    private ArrayList<String> roomsDataset;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerGridView;
    private GridLayoutManager gridLayoutManager;
    private ListArtifactsAdapter listArtifactsAdapter;
    private ArrayList<String> mDataset;
    private boolean selected = false;
    private boolean add;
    private int roomId;
    private final Place place;

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
                    // TODO: Scan QR Code Here (Intent with camera)
                    break;
            }
            return true;
        });

        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void setSelectElement() {
        //TODO: Read Room API
        roomsDataset = new ArrayList<>();
        roomsDataset.add("Stanza A");
        roomsDataset.add("Stanza B");
        roomsDataset.add("Stanza C");
        arrayOptionsAdapter = new ArrayAdapter(getContext(), R.layout.item_select_room, roomsDataset);
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(arrayOptionsAdapter);
    }

    private void setDynamicRecycleView() {
        recyclerGridView = view.findViewById(R.id.recyclerContainerObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerGridView.setLayoutManager(gridLayoutManager);
        listArtifactsAdapter = new ListArtifactsAdapter(mDataset);
        recyclerGridView.setAdapter(listArtifactsAdapter);
    }

    public void onStart() {
        super.onStart();

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if(autoCompleteTextView.getInputType() != EditorInfo.TYPE_NULL)
                autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);

            if(autoCompleteTextView.getError() != null)
                autoCompleteTextView.setError(null);

            selected = true;
            roomId = position;
        });

        autoCompleteTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            String name = textView.getText().toString();
            if(checkRoomName(name)) {
                autoCompleteTextView.setError(null);
                roomsDataset.add(name);
                if(add) {
                    // TODO: Insert Room API
                } else {
                    roomsDataset.remove(roomId);
                    // TODO: Update Room API
                }
            } else {
                autoCompleteTextView.setError(getString(R.string.invalid_room));
            }

            arrayOptionsAdapter.clear();
            arrayOptionsAdapter.addAll(roomsDataset);
            arrayOptionsAdapter.notifyDataSetChanged();

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
            roomsDataset.remove(roomId);
            autoCompleteTextView.setText("");
            selected = false;
            // TODO: Delete Room API
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

}