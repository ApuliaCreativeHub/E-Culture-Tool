package com.apuliacreativehub.eculturetool.ui.places;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Artifact;

import java.util.ArrayList;

public class CreatePathFragment extends Fragment {

    private static final int NUMBER_COLUMN = 2;

    private View view;
    private ArrayAdapter arrayOptionsAdapter;
    private AutoCompleteTextView autoCompleteTextView;

    private RecyclerView recyclerArtifactsGridView;
    private RecyclerView recyclerArtifactsCircleLinearView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ListArtifactsCreateAdapter listArtifactsCreateAdapter;
    private ListCircleArtifactsAdapter listCircleArtifactsAdapter;
    private ArrayList<Artifact> mCircleArtifactDataset;
    private ArrayList<Artifact> mArtifactDataset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Artifact testArtifact = new Artifact(100, "Opera d'arte", "Opera d'arte antica", "img");
        Artifact testArtifact2 = new Artifact(101, "Opera d'arte 2", "Opera d'arte antica 2", "img2");

        mCircleArtifactDataset = new ArrayList<>();
        mArtifactDataset = new ArrayList<>();
        mArtifactDataset.add(testArtifact);
        mArtifactDataset.add(testArtifact2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_path, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        setSelectElement();
        setDynamicCircleArtifactRecycleView();
        setDynamicArtifactRecycleView();
        Toolbar toolbar = view.findViewById(R.id.createPathToolbar);
        toolbar.setTitle(R.string.create_place_path);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

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
        listArtifactsCreateAdapter = new ListArtifactsCreateAdapter(R.layout.component_card_link_artifact, mArtifactDataset, mCircleArtifactDataset, listCircleArtifactsAdapter, getContext());
        recyclerArtifactsGridView.setAdapter(listArtifactsCreateAdapter);
    }

    private void setDynamicCircleArtifactRecycleView() {
        recyclerArtifactsCircleLinearView = view.findViewById(R.id.recyclerCircleArtifact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerArtifactsCircleLinearView.setLayoutManager(linearLayoutManager);
        listCircleArtifactsAdapter = new ListCircleArtifactsAdapter(mCircleArtifactDataset);
        recyclerArtifactsCircleLinearView.setAdapter(listCircleArtifactsAdapter);
    }

}