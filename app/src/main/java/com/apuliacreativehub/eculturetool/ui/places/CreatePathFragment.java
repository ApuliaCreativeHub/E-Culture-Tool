package com.apuliacreativehub.eculturetool.ui.places;

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
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.apuliacreativehub.eculturetool.data.entity.Object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class CreatePathFragment extends Fragment {
    private static final int NUMBER_COLUMN = 2;
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;

    private View view;
    private ArrayAdapter arrayOptionsAdapter;
    private AutoCompleteTextView autoCompleteTextView;

    private FloatingActionButton confirmFab;
    private RecyclerView recyclerArtifactsGridView;
    private RecyclerView recyclerArtifactsCircleLinearView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ListObjectsCreateAdapter listArtifactsCreateAdapter;
    private ListCircleObjectsAdapter listCircleObjectsAdapter;
    private MutableGraph<NodeArtifact> graphArtifactDataset;
    private ArrayList<NodeArtifact> mArtifactDataset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphArtifactDataset = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .nodeOrder(ElementOrder.sorted(new Comparator<NodeArtifact>() {

                    @Override
                    public int compare(NodeArtifact o1, NodeArtifact o2) {
                        if(o1.getWeight() - o2.getWeight() > 0) return 1;
                        if(o1.getWeight() - o2.getWeight() < 0) return -1;
                        return 0;
                    }
                }))
                .incidentEdgeOrder(ElementOrder.stable())
                .build();


        //TODO: FETCH ARTIFACT OF PLACE
        NodeArtifact testArtifact = new NodeArtifact(100, "AAA", "Opera d'arte antica",  "img1", 1);
        NodeArtifact testArtifact2 = new NodeArtifact(101, "BBB", "Opera d'arte antica 2", "img1",2);
        NodeArtifact testArtifact3 = new NodeArtifact(102, "CCC", "Opera d'arte antica 3", "img1",3);
        mArtifactDataset = new ArrayList<>();
        mArtifactDataset.add(testArtifact);
        mArtifactDataset.add(testArtifact2);
        mArtifactDataset.add(testArtifact3);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_path, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSelectElement();
        setDynamicCircleArtifactRecycleView();
        setDynamicArtifactRecycleView();
        Toolbar toolbar = view.findViewById(R.id.createPathToolbar);
        toolbar.setTitle(R.string.create_place_path);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onStart() {
        super.onStart();
        confirmFab = view.findViewById(R.id.btnCreatePath);

        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreatePath();
            }
        });
    }

    private void setSelectElement() {
        //TODO: Fetch room
        arrayOptionsAdapter = new ArrayAdapter(getContext(), R.layout.item_select_room, new String[]{"Stanza A", "Stanza B", " Stanza C"});
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(arrayOptionsAdapter);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void setDynamicArtifactRecycleView() {
        recyclerArtifactsGridView = view.findViewById(R.id.recyclerContainerCreateObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerArtifactsGridView.setLayoutManager(gridLayoutManager);
        listArtifactsCreateAdapter = new ListObjectsCreateAdapter(R.layout.component_card_link_artifact, mArtifactDataset, graphArtifactDataset, listCircleObjectsAdapter, getContext());
        recyclerArtifactsGridView.setAdapter(listArtifactsCreateAdapter);
    }

    private void setDynamicCircleArtifactRecycleView() {
        recyclerArtifactsCircleLinearView = view.findViewById(R.id.recyclerCircleArtifact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerArtifactsCircleLinearView.setLayoutManager(linearLayoutManager);
        listCircleObjectsAdapter = new ListCircleObjectsAdapter(graphArtifactDataset);
        recyclerArtifactsCircleLinearView.setAdapter(listCircleObjectsAdapter);
    }


    private void handleCreatePath() {
        EditText txtPathName = view.findViewById(R.id.txtPathName);
        String pathName = txtPathName.getText().toString();


        if(checkPathName(pathName)) {
            NodeArtifact[] rawResultsGraph = graphArtifactDataset.nodes().toArray(new NodeArtifact[0]);

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