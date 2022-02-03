package com.apuliacreativehub.eculturetool.ui.paths.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.paths.viewmodel.EditPathViewModel;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListCircleObjectsAdapter;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListObjectsCreateAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

public class EditPathFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    private static final int NUMBER_COLUMN = 2;
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;

    private final Place place;
    private final Path path;
    private View view;
    private AutoCompleteTextView autoCompleteTextView;
    private EditPathViewModel editPathViewModel;
    private RecyclerView recyclerObjectsGridView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerObjectsCircleLinearView;
    private ListObjectsCreateAdapter listObjectsCreateAdapter;
    private ListCircleObjectsAdapter listCircleObjectsAdapter;
    final Observer<RepositoryNotification<Path>> savePathObserver = new Observer<RepositoryNotification<Path>>() {
        @Override
        public void onChanged(RepositoryNotification<Path> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null || notification.getErrorMessage().isEmpty()) {
                    requireActivity().getSupportFragmentManager().popBackStackImmediate();
                } else {
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "UPDATING_PATH_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "UPDATING_PATH_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<HashMap<String, List<NodeObject>>>> readyDatasetObserver = new Observer<RepositoryNotification<HashMap<String, List<NodeObject>>>>() {
        @Override
        public void onChanged(RepositoryNotification<HashMap<String, List<NodeObject>>> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    setSelectElement();
                    setDynamicArtifactRecycleView();
                    editPathViewModel.setGraphDatasetFromOrderedObjects();
                    setDynamicCircleArtifactRecycleView();
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

    public EditPathFragment(Place place, Path path) {
        this.place = place;
        this.path = path;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editPathViewModel = new ViewModelProvider(this).get(EditPathViewModel.class);
        editPathViewModel.setPlace(place);
        editPathViewModel.setPath(path);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_path, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.editPathToolbar);
        toolbar.setTitle(R.string.edit_path_screen_title);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v ->
                new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_discard_path_changes), "DISCARD_PATH_CHANGES").show(getChildFragmentManager(), Dialog.TAG)
        );
    }

    @Override
    public void onStart() {
        super.onStart();

        editPathViewModel.getObjectsDataset().observe(this, readyDatasetObserver);

        FloatingActionButton confirmFab = view.findViewById(R.id.btnEditPath);
        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreatePath();
            }
        });
    }

    private void setSelectElement() {
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.component_item_select_room, editPathViewModel.getZoneNames()));
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoCompleteTextView.getInputType() != EditorInfo.TYPE_NULL)
                    autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);

                if (autoCompleteTextView.getError() != null)
                    autoCompleteTextView.setError(null);

                editPathViewModel.setCurrentlySelectedZoneName((String) ((TextView) view.findViewById(R.id.zoneName)).getText());
                Log.i("CurrentZone", editPathViewModel.getCurrentlySelectedZoneName());
                setDynamicArtifactRecycleView();
            }
        });
    }

    private void setDynamicArtifactRecycleView() {
        recyclerObjectsGridView = view.findViewById(R.id.recyclerContainerCreateObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerObjectsGridView.setLayoutManager(gridLayoutManager);
        listObjectsCreateAdapter = new ListObjectsCreateAdapter(requireContext(), R.layout.component_card_link_artifact, editPathViewModel, listCircleObjectsAdapter);
        recyclerObjectsGridView.setAdapter(listObjectsCreateAdapter);
    }

    private void setDynamicCircleArtifactRecycleView() {
        recyclerObjectsCircleLinearView = view.findViewById(R.id.recyclerCircleArtifact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerObjectsCircleLinearView.setLayoutManager(linearLayoutManager);
        listCircleObjectsAdapter = new ListCircleObjectsAdapter(editPathViewModel.getGraphDataset(), getContext());
        recyclerObjectsCircleLinearView.setAdapter(listCircleObjectsAdapter);
    }


    private void handleCreatePath() {
        EditText txtPathName = view.findViewById(R.id.txtPathName);
        String pathName = txtPathName.getText().toString();

        if(checkPathName(pathName)) {
            editPathViewModel.setPathName(pathName);
            NodeObject[] rawResultsGraph = editPathViewModel.getGraphDataset().nodes().toArray(new NodeObject[0]);

            if (rawResultsGraph.length > 0) {
                //HASHMAP VALUES: ORDER INTO THE GRAPH - ID OF THE OBJECT
                HashMap<Integer, Integer> graphIdPath = new HashMap<>();
                for (int i = 0; i < rawResultsGraph.length; i++) {
                    graphIdPath.put(i, rawResultsGraph[i].getId());
                }
                //TO GET FROM JSON TO HASHMAP: (WE NEED AN ARRAY OF ARTIFACT NOT OBJECT LIKE THIS, IS ONLY AN EXAMPLE)
                editPathViewModel.setOrderedObjects(graphIdPath);
                if (editPathViewModel.getPath().getUserId() == editPathViewModel.getPlace().getUserId()) {
                    try {
                        editPathViewModel.editPath().observe(this, savePathObserver);
                    } catch (NoInternetConnectionException e) {
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), "NO_INTERNET_CONNECTION_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                    }
                } else {
                    try {
                        editPathViewModel.addPath().observe(this, savePathObserver);
                    } catch (NoInternetConnectionException e) {
                        new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), "NO_INTERNET_CONNECTION_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                    }
                }
            } else {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.path_error), "PATH_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            txtPathName.setError(getString(R.string.invalid_path));
        }
    }

    private boolean checkPathName(String name) {
        return name.length() >= MIN_LENGTH_NAME && name.length() <= MAX_LENGTH_NAME;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}