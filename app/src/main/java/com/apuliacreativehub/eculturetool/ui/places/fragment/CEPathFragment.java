package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListCircleObjectsAdapter;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListObjectsCreateAdapter;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CEPathViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

public abstract class CEPathFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener {
    protected static final int NUMBER_COLUMN = 2;
    protected static final int MIN_LENGTH_NAME = 2;
    protected static final int MAX_LENGTH_NAME = 25;
    public final Observer<RepositoryNotification<Path>> savePathObserver = new Observer<RepositoryNotification<Path>>() {
        @Override
        public void onChanged(RepositoryNotification<Path> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null || notification.getErrorMessage().isEmpty()) {
                    onSaveSuccessful();
                } else {
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "UPDATING_PATH_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "UPDATING_PATH_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };
    protected final Place place;
    protected View view;
    protected AutoCompleteTextView autoCompleteTextView;
    protected CEPathViewModel cePathViewModel;
    protected RecyclerView recyclerObjectsGridView;
    protected GridLayoutManager gridLayoutManager;
    protected LinearLayoutManager linearLayoutManager;
    protected RecyclerView recyclerObjectsCircleLinearView;
    protected ListObjectsCreateAdapter listObjectsCreateAdapter;
    protected ListCircleObjectsAdapter listCircleObjectsAdapter;
    protected AdapterView.OnItemClickListener adapterViewOnItemClickListener;
    Observer<RepositoryNotification<HashMap<String, List<NodeObject>>>> readyDatasetObserver;

    public CEPathFragment(Place place) {
        this.place = place;
    }

    protected void setReadyDatasetObserver(Observer<RepositoryNotification<HashMap<String, List<NodeObject>>>> readyDatasetObserver) {
        this.readyDatasetObserver = readyDatasetObserver;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_build_path, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.buildPathToolbar);
        setToolbarTitle(toolbar);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v ->
                new ConfirmationDialog(getString(R.string.warning_dialog_title), getString(R.string.warning_discard_path_changes), "DISCARD_PATH_CHANGES").show(getChildFragmentManager(), Dialog.TAG)
        );
    }

    protected abstract void setToolbarTitle(Toolbar toolbar);

    @Override
    public void onStart() {
        super.onStart();

        cePathViewModel.getObjectsDataset().observe(this, readyDatasetObserver);

        FloatingActionButton confirmFab = view.findViewById(R.id.btnSavePath);
        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreatePath();
            }
        });
    }

    protected abstract void initializeViewModel();

    protected void setAdapterViewOnItemClickListener(AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        this.adapterViewOnItemClickListener = adapterViewOnItemClickListener;
    }

    protected void setSelectElement() {
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.component_item_select_room, cePathViewModel.getZoneNames()));
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(adapterViewOnItemClickListener);
    }

    protected void setDynamicArtifactRecycleView() {
        recyclerObjectsGridView = view.findViewById(R.id.recyclerContainerBuildObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerObjectsGridView.setLayoutManager(gridLayoutManager);
        listObjectsCreateAdapter = new ListObjectsCreateAdapter(requireContext(), R.layout.component_card_link_artifact, cePathViewModel, listCircleObjectsAdapter);
        recyclerObjectsGridView.setAdapter(listObjectsCreateAdapter);
    }

    protected void setDynamicCircleArtifactRecycleView() {
        recyclerObjectsCircleLinearView = view.findViewById(R.id.recyclerCircleArtifact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerObjectsCircleLinearView.setLayoutManager(linearLayoutManager);
        listCircleObjectsAdapter = new ListCircleObjectsAdapter(cePathViewModel.getGraphDataset(), getContext());
        recyclerObjectsCircleLinearView.setAdapter(listCircleObjectsAdapter);
    }


    private void handleCreatePath() {
        EditText txtPathName = view.findViewById(R.id.txtPathName);
        String pathName = txtPathName.getText().toString();

        if (checkPathName(pathName)) {
            cePathViewModel.setPathName(pathName);
            NodeObject[] rawResultsGraph = cePathViewModel.getGraphDataset().nodes().toArray(new NodeObject[0]);

            if (rawResultsGraph.length > 0) {
                //HASHMAP VALUES: ORDER INTO THE GRAPH - ID OF THE OBJECT
                HashMap<Integer, Integer> graphIdPath = new HashMap<>();
                for (int i = 0; i < rawResultsGraph.length; i++) {
                    graphIdPath.put(i, rawResultsGraph[i].getId());
                }
                //TO GET FROM JSON TO HASHMAP: (WE NEED AN ARRAY OF ARTIFACT NOT OBJECT LIKE THIS, IS ONLY AN EXAMPLE)
                cePathViewModel.setOrderedObjects(graphIdPath);
                persistPath();
            } else {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.path_error), "PATH_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            txtPathName.setError(getString(R.string.invalid_path));
        }
    }

    protected abstract void persistPath();

    private boolean checkPathName(String name) {
        return name.length() >= MIN_LENGTH_NAME && name.length() <= MAX_LENGTH_NAME;
    }

    @Override
    public abstract void onDialogPositiveClick(DialogFragment dialog);

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    protected abstract void onSaveSuccessful();
}
