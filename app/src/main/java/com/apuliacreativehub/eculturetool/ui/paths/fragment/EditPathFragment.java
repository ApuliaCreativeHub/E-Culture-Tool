package com.apuliacreativehub.eculturetool.ui.paths.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.GuavaHelper;
import com.apuliacreativehub.eculturetool.ui.places.NodeArtifact;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListCircleObjectsAdapter;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListObjectsCreateAdapter;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CreatePathViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.graph.MutableGraph;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

public class EditPathFragment extends Fragment {
    private static final int NUMBER_COLUMN = 2;
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;

    private View view;
    private AutoCompleteTextView autoCompleteTextView;

    private CreatePathViewModel createPathViewModel;
    private ListCircleObjectsAdapter circleObjectsAdapter;
    private HashMap<String, ListObjectsCreateAdapter> objectsAdapters;

    private FloatingActionButton confirmFab;
    private RecyclerView recyclerArtifactsGridView;
    private RecyclerView recyclerArtifactsCircleLinearView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPathViewModel = new CreatePathViewModel(requireContext());
        circleObjectsAdapter = new ListCircleObjectsAdapter(createPathViewModel.getGraphDataset());
        objectsAdapters = new HashMap<>();
        for(Zone zone: createPathViewModel.getListZone())
            objectsAdapters.put(zone.getName(), new ListObjectsCreateAdapter(R.layout.component_card_link_artifact, createPathViewModel, circleObjectsAdapter, zone.getName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_path, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDynamicCircleArtifactRecycleView();
        setDynamicArtifactRecycleView();
        setSelectElement();
        Toolbar toolbar = view.findViewById(R.id.editPathToolbar);
        toolbar.setTitle(R.string.edit_place_screen_title);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    @Override
    public void onStart() {
        super.onStart();
        confirmFab = view.findViewById(R.id.btnEditPath);
        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreatePath();
            }
        });
    }

    private void setSelectElement() {
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(new ArrayAdapter(getContext(), R.layout.component_item_select_room, createPathViewModel.getListStringZone().toArray()));
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerArtifactsGridView.setAdapter(objectsAdapters.get(createPathViewModel.getListZone().get(position).getName()));
            }
        });
    }

    private void setDynamicArtifactRecycleView() {
        recyclerArtifactsGridView = view.findViewById(R.id.recyclerContainerCreateObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerArtifactsGridView.setLayoutManager(gridLayoutManager);
    }

    private void setDynamicCircleArtifactRecycleView() {
        recyclerArtifactsCircleLinearView = view.findViewById(R.id.recyclerCircleArtifact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerArtifactsCircleLinearView.setLayoutManager(linearLayoutManager);
        recyclerArtifactsCircleLinearView.setAdapter(circleObjectsAdapter);
    }

    private void handleCreatePath() {
        EditText txtPathName = view.findViewById(R.id.txtPathName);
        String pathName = txtPathName.getText().toString();


        if(checkPathName(pathName)) {
            NodeArtifact[] rawResultsGraph = createPathViewModel.getGraphDataset().nodes().toArray(new NodeArtifact[0]);

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

                requireActivity().finish();
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