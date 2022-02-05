package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.PathRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.di.ECultureTool;
import com.apuliacreativehub.eculturetool.ui.component.GuavaHelper;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CEPathViewModel extends ZoneObjectViewModel {
    protected final PathRepository pathRepository;
    private final MutableGraph<NodeObject> graphDataset;
    private String pathName;
    private Map<Integer, Integer> orderedObjects;
    private final HashMap<String, List<NodeObject>> tempObjectsDataset = new HashMap<>();
    private NodeObject utilNodeTemp;
    private MutableLiveData<RepositoryNotification<HashMap<String, List<NodeObject>>>> objectsDataset;
    public Observer<RepositoryNotification<ArrayList<Zone>>> getZonesObserver = new Observer<RepositoryNotification<ArrayList<Zone>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Zone>> notification) {
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null) {
                    getZoneNames().clear();
                    setZones(notification.getData());
                    for (Zone zone : notification.getData()) {
                        objectRepository.getObjects(zone).observeForever(getObjectsObserver);
                    }
                } else {
                    RepositoryNotification<HashMap<String, List<NodeObject>>> err = new RepositoryNotification<>();
                    err.setErrorMessage(notification.getErrorMessage());
                    objectsDataset.postValue(err);
                }
            } else {
                RepositoryNotification<HashMap<String, List<NodeObject>>> err = new RepositoryNotification<>();
                err.setException(notification.getException());
                objectsDataset.postValue(err);
            }
        }
    };

    private int i = 0;
    public Observer<RepositoryNotification<ArrayList<Object>>> getObjectsObserver = new Observer<RepositoryNotification<ArrayList<Object>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Object>> notification) {
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null) {
                    if (notification.getData().size() != 0) {
                        Zone zone = getZoneById(notification.getData().get(0).getZoneId());
                        tempObjectsDataset.put(zone.getName(), NodeObject.getNodeObjectAll(notification.getData()));
                    }
                    i++;
                    if (i == getZones().size()) {
                        setObjectsDatasetForNotification();
                    }
                } else {
                    RepositoryNotification<HashMap<String, List<NodeObject>>> err = new RepositoryNotification<>();
                    err.setErrorMessage(notification.getErrorMessage());
                    objectsDataset.postValue(err);
                }
            } else {
                RepositoryNotification<HashMap<String, List<NodeObject>>> err = new RepositoryNotification<>();
                err.setException(notification.getException());
                objectsDataset.postValue(err);
            }
        }
    };

    public CEPathViewModel(@NonNull Application application) {
        super(application);
        ECultureTool app = (ECultureTool) application;
        pathRepository = new PathRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
        graphDataset = GuavaHelper.createInstance();
    }

    public MutableLiveData<RepositoryNotification<HashMap<String, List<NodeObject>>>> getObjectsDataset() {
        if (objectsDataset == null) {
            objectsDataset = new MutableLiveData<>();
            populateObjectsDataset();
        }
        return objectsDataset;
    }

    private void setObjectsDatasetForNotification() {
        RepositoryNotification<HashMap<String, List<NodeObject>>> notification = new RepositoryNotification<>();
        notification.setData(tempObjectsDataset);
        objectsDataset.postValue(notification);
    }

    public void populateObjectsDataset() {
        if (super.getPlace() != null) {
            zoneRepository.getAllPlaceZones(super.getPlace()).observeForever(getZonesObserver);
        }
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

    public NodeObject getUtilNodeTemp() {
        return utilNodeTemp;
    }

    public void setUtilNodeTemp(NodeObject utilNodeTemp) {
        this.utilNodeTemp = utilNodeTemp;
    }

    /**
     * If utilNodeTemp is null means that the graph is empty. -> We have to add a node. (to trace operation we save the last value added to node in a variable)
     * if utilNodeTemp is not null means that there is almost one node -> We have to link the new node to the last node.
     *
     * @param newNode
     */
    protected void increaseGraph(NodeObject newNode) {
        if (getUtilNodeTemp() != null) {
            newNode.setWeight(getUtilNodeTemp().getWeight() * 2);
            getGraphDataset().putEdge(getUtilNodeTemp(), newNode);
        } else {
            newNode.setWeight(1.0);
            getGraphDataset().addNode(newNode);
        }
        setUtilNodeTemp(newNode);
    }

    public NodeObject findObjectByIdInObjectsDataset(int objectId) {
        for (List<NodeObject> zoneNodeObjects : objectsDataset.getValue().getData().values()) {
            for (NodeObject nodeObject : zoneNodeObjects) {
                if (nodeObject.getId() == objectId) {
                    return nodeObject;
                }
            }
        }
        return null;
    }

    public NodeObject getNodeObjectFromObjectsDatasetById(int objectId) {
        return findObjectByIdInObjectsDataset(objectId);
    }

    public MutableLiveData<RepositoryNotification<Path>> addPath() throws NoInternetConnectionException {
        Path path = new Path(getPathName());
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < getOrderedObjects().size(); i++) {
            int objectId = getOrderedObjects().get(i);
            objects.add(getNodeObjectFromObjectsDatasetById(objectId));
        }
        path.setPlace(getPlace());
        path.setObjects(objects);
        return pathRepository.addPath(path);
    }
}
