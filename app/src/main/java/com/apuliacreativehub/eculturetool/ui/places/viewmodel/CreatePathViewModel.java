package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.ui.component.GuavaHelper;
import com.apuliacreativehub.eculturetool.ui.places.NodeArtifact;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListCircleObjectsAdapter;
import com.apuliacreativehub.eculturetool.ui.places.adapter.ListObjectsCreateAdapter;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatePathViewModel extends ViewModel {

    private MutableGraph<NodeArtifact> graphDataset;
    private HashMap<String, ArrayList<NodeArtifact>> objectsDataset;

    private ArrayList<Zone> listZone;
    private ArrayList<String> listStringZone;
    private Context context;

    private NodeArtifact utilNodeTemp;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HashMap<String, ArrayList<NodeArtifact>> getObjectsDataset() {
        return objectsDataset;
    }

    private void setObjectsDataset() {
        objectsDataset = new HashMap<>();
        ArrayList<NodeArtifact> listRoom1 = new ArrayList<>();
        NodeArtifact testArtifact = new NodeArtifact(100, "AAA", "Opera d'arte antica",  "img1", 1);
        NodeArtifact testArtifact2 = new NodeArtifact(101, "BBB", "Opera d'arte antica 2", "img1",2);
        NodeArtifact testArtifact3 = new NodeArtifact(102, "CCC", "Opera d'arte antica 3", "img1",3);
        NodeArtifact testArtifact4 = new NodeArtifact(103, "DDD", "Opera d'arte antica 4", "img1",3);
        listRoom1.add(testArtifact);
        listRoom1.add(testArtifact2);
        listRoom1.add(testArtifact3);
        listRoom1.add(testArtifact4);

        objectsDataset.put(listZone.get(0).getName(), listRoom1);
        ArrayList<NodeArtifact> listRoom2 = new ArrayList<>();
        NodeArtifact testArtifact5 = new NodeArtifact(104, "EEE", "Opera d'arte antica",  "img1", 1);
        NodeArtifact testArtifact6 = new NodeArtifact(105, "FFF", "Opera d'arte antica 2", "img1",2);
        NodeArtifact testArtifact7 = new NodeArtifact(106, "GGG", "Opera d'arte antica 3", "img1",3);
        NodeArtifact testArtifact8 = new NodeArtifact(107, "HHH", "Opera d'arte antica 4", "img1",3);
        listRoom2.add(testArtifact5);
        listRoom2.add(testArtifact6);
        listRoom2.add(testArtifact7);
        listRoom2.add(testArtifact8);

        objectsDataset.put(listZone.get(1).getName(), listRoom2);
    }

    public ArrayList<Zone> getListZone() {
        return listZone;
    }

    private void setListZone() {
        listZone = new ArrayList<>();
        listZone.add(new Zone(1, "Stanza A", 300));
        listZone.add(new Zone(2, "Stanza B", 300));
    }

    private void setListStringZone() {
        this.listStringZone = new ArrayList<>();
        for(Zone zone: listZone)
            listStringZone.add(zone.getName());
    }

    public ArrayList<String> getListStringZone() {
        return this.listStringZone;
    }

    public MutableGraph<NodeArtifact> getGraphDataset() {
        return graphDataset;
    }

    public NodeArtifact getUtilNodeTemp() {
        return utilNodeTemp;
    }

    public void setUtilNodeTemp(NodeArtifact utilNodeTemp) {
        this.utilNodeTemp = utilNodeTemp;
    }

    public CreatePathViewModel(Context context) {
        this.context = context;
        graphDataset = GuavaHelper.createInstance();
        setListZone();
        setListStringZone();
        setObjectsDataset();
        setUtilNodeTemp(null);
    }
}
