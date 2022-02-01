package com.apuliacreativehub.eculturetool.ui.places.fragment;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListCircleObjectsAdapter;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListObjectsCreateAdapter;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CreatePathViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.graph.MutableGraph;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePathFragment extends Fragment {
    private static final int NUMBER_COLUMN = 2;
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;
    private final Place place;

    private View view;
    private ArrayAdapter<String> arrayOptionsAdapter;
    private AutoCompleteTextView autoCompleteTextView;

    private CreatePathViewModel createPathViewModel;
    private ListCircleObjectsAdapter circleObjectsAdapter;
    private HashMap<String, ListObjectsCreateAdapter> objectsAdapters;

    private FloatingActionButton confirmFab;
    private RecyclerView recyclerObjectsGridView;
    private RecyclerView recyclerObjectsCircleLinearView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ListObjectsCreateAdapter listObjectsCreateAdapter;
    private ListCircleObjectsAdapter listCircleObjectsAdapter;
    private MutableGraph<NodeObject> graphArtifactDataset;
    private ArrayList<NodeObject> mArtifactDataset;

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

    public CreatePathFragment(Place place){
        this.place = place;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_path, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createPathViewModel = new ViewModelProvider(this).get(CreatePathViewModel.class);
        createPathViewModel.setPlace(place);
        objectsAdapters = new HashMap<>();

        Toolbar toolbar = view.findViewById(R.id.createPathToolbar);
        toolbar.setTitle(R.string.create_place_path);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onStart() {
        super.onStart();

        createPathViewModel.getObjectsDataset().observe(this, readyDatasetObserver);

        confirmFab = view.findViewById(R.id.btnCreatePath);
        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreatePath();
            }
        });
    }

    private void setSelectElement() {
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.component_item_select_room, createPathViewModel.getZoneNames()));
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerObjectsGridView.setAdapter(objectsAdapters.get(createPathViewModel.getZones().get(position).getName()));
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoCompleteTextView.getInputType() != EditorInfo.TYPE_NULL)
                    autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);

                if (autoCompleteTextView.getError() != null)
                    autoCompleteTextView.setError(null);

                createPathViewModel.setCurrentlySelectedZoneName((String) ((TextView) view.findViewById(R.id.zoneName)).getText());
                Log.i("CurrentZone", createPathViewModel.getCurrentlySelectedZoneName());
                setDynamicArtifactRecycleView();
            }
        });
    }

    private void setDynamicArtifactRecycleView() {
        recyclerObjectsGridView = view.findViewById(R.id.recyclerContainerCreateObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerObjectsGridView.setLayoutManager(gridLayoutManager);
        listObjectsCreateAdapter = new ListObjectsCreateAdapter(requireContext(), R.layout.component_card_link_artifact, createPathViewModel, listCircleObjectsAdapter);
        recyclerObjectsGridView.setAdapter(listObjectsCreateAdapter);
    }

    private void setDynamicCircleArtifactRecycleView() {
        recyclerObjectsCircleLinearView = view.findViewById(R.id.recyclerCircleArtifact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerObjectsCircleLinearView.setLayoutManager(linearLayoutManager);
        listCircleObjectsAdapter = new ListCircleObjectsAdapter(createPathViewModel.getGraphDataset(), getContext());
        recyclerObjectsCircleLinearView.setAdapter(listCircleObjectsAdapter);
    }


    private void handleCreatePath() {
        EditText txtPathName = view.findViewById(R.id.txtPathName);
        String pathName = txtPathName.getText().toString();


        if(checkPathName(pathName)) {
            NodeObject[] rawResultsGraph = createPathViewModel.getGraphDataset().nodes().toArray(new NodeObject[0]);

            if(rawResultsGraph.length > 0) {
                //HASHMAP VALUES: ORDER INTO THE GRAPH - ID OF THE OBJECT
                HashMap<Integer, Integer> graphIdPath = new HashMap<>();
                for (int i = 0; i < rawResultsGraph.length; i++) {
                    graphIdPath.put(i, rawResultsGraph[i].getId());
                }
                Gson gson = new Gson();
                JsonElement resultJson = gson.toJsonTree(graphIdPath, HashMap.class);
                Log.i("JSON?", resultJson.toString());

                //TO GET FROM JSON TO HASHMAP: (WE NEED AN ARRAY OF ARTIFACT NOT OBJECT LIKE THIS, IS ONLY AN EXAMPLE)
                //HashMap<Integer, Integer> result = gson.fromJson(newJson, HashMap.class);
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

}