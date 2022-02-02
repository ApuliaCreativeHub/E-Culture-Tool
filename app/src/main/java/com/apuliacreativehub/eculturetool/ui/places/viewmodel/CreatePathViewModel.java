package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePathViewModel extends CEPathViewModel {
    private MutableLiveData<RepositoryNotification<HashMap<String, List<NodeObject>>>> objectsDataset;
    private final HashMap<String, List<NodeObject>> tempObjectsDataset = new HashMap<>();

    public CreatePathViewModel(Application application) {
        super(application);
    }

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
                    if (i == CreatePathViewModel.super.getZones().size()) {
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

    private int i = 0;
    public Observer<RepositoryNotification<ArrayList<Zone>>> getZonesObserver = new Observer<RepositoryNotification<ArrayList<Zone>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Zone>> notification) {
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null) {
                    CreatePathViewModel.super.getZoneNames().clear();
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
    private NodeObject utilNodeTemp;

    public MutableLiveData<RepositoryNotification<HashMap<String, List<NodeObject>>>> getObjectsDataset() {
        if(objectsDataset == null) {
            objectsDataset = new MutableLiveData<>();
            populateObjectsDataset();
        }
        return objectsDataset;
    }

    public NodeObject getUtilNodeTemp() {
        return utilNodeTemp;
    }

    public void setUtilNodeTemp(NodeObject utilNodeTemp) {
        this.utilNodeTemp = utilNodeTemp;
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

    public MutableLiveData<RepositoryNotification<Path>> addPath() throws NoInternetConnectionException {
        Path path = new Path(super.getPathName());
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < super.getOrderedObjects().size(); i++) {
            int objectId = super.getOrderedObjects().get(i);
            objects.add(new Object(objectId));
        }
        path.setObjects(objects);
        return super.pathRepository.addPath(path);
    }
}
