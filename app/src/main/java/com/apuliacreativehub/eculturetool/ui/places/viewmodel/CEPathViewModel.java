package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

import com.apuliacreativehub.eculturetool.data.repository.PathRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.Map;

public class CEPathViewModel extends SomethingViewModel {
    protected final PathRepository pathRepository;
    private final MutableGraph<NodeObject> graphDataset;
    private String pathName;
    private Map<Integer, Integer> orderedObjects;

    public CEPathViewModel(@NonNull Application application) {
        super(application);
        ECultureTool app = (ECultureTool) application;
        pathRepository = new PathRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
        graphDataset = GraphBuilder.directed().build();
    }

    public MutableGraph<NodeObject> getGraphDataset() {
        return graphDataset;
    }

    public Map<Integer, Integer> getOrderedObjects() {
        return orderedObjects;
    }

    public void setOrderedObjects(Map<Integer, Integer> orderedObjects) {
        this.orderedObjects = orderedObjects;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
