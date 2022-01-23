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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.ListArtifactsAdapter;

import java.util.ArrayList;

public class CreatePathFragment extends Fragment {

    private static final int NUMBER_COLUMN = 2;

    private View view;
    private ArrayAdapter arrayOptionsAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerGridView;
    private GridLayoutManager gridLayoutManager;
    private ListArtifactsAdapter listArtifactsAdapter;
    private ArrayList<String> mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();
        mDataset.add("CIAO");
        mDataset.add("CIAO1");
        mDataset.add("CIAO2");
        mDataset.add("CIAO3");
        mDataset.add("CIAO4");
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
        setDynamicRecycleView();
        Toolbar toolbar = view.findViewById(R.id.createPathToolbar);
        toolbar.setTitle(R.string.create_place_path);
        toolbar.setNavigationIcon(R.mipmap.outline_arrow_back_ios_black_24);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void setDynamicRecycleView() {
        recyclerGridView = view.findViewById(R.id.recyclerContainerObject);
        gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN);
        recyclerGridView.setLayoutManager(gridLayoutManager);
        listArtifactsAdapter = new ListArtifactsAdapter(mDataset);
        recyclerGridView.setAdapter(listArtifactsAdapter);

    }

    private void setSelectElement() {
        //TODO: Fetch room
        arrayOptionsAdapter = new ArrayAdapter(getContext(), R.layout.item_select_room, new String[]{"Stanza A", "Stanza B", " Stanza C"});
        autoCompleteTextView = view.findViewById(R.id.selectRoomAutoComplete);
        autoCompleteTextView.setAdapter(arrayOptionsAdapter);

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("ENTER", "OK");
                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("CLICK", "OK");
            }
        });
    }

    /**
     * registerForContextMenu(textInputLayout);
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("PROVA1");
        menu.add("PROVA2");
    }
    **/
}