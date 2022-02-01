package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.ObjectRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.data.repository.ZoneRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePathViewModel extends AndroidViewModel {
    private final ZoneRepository zoneRepository;
    private final ObjectRepository objectRepository;
    private List<Zone> zones;
    private final List<String> zoneNames;
    private Place place;
    private String currentlySelectedZoneName = "";
    private final HashMap<String, List<NodeObject>> tempObjectsDataset = new HashMap<>();
    private MutableGraph<NodeObject> graphDataset;
    private MutableLiveData<RepositoryNotification<HashMap<String, List<NodeObject>>>> objectsDataset;

    public CreatePathViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        zoneNames = new ArrayList<>();
        zoneRepository = new ZoneRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
        objectRepository = new ObjectRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
        graphDataset = GraphBuilder.directed().build();
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getZonesFromDatabase() {
        if (place != null) {
            return zoneRepository.getAllPlaceZones(place);
        } else {
            return new MutableLiveData<>();
        }
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;

        for(Zone zone : zones){
            zoneNames.add(zone.getName());
        }
    }

    public List<String> getZoneNames() {
        return zoneNames;
    }

    public Zone getZoneById(int id) {
        for (Zone zone : zones) {
            if (zone.getId() == id) return zone;
        }
        return null;
    }

    public Zone getZoneByName(String name) {
        for (Zone zone : zones) {
            if (zone.getName().equals(name)) return zone;
        }
        return null;
    }

    public String getCurrentlySelectedZoneName() {
        return currentlySelectedZoneName;
    }

    public void setCurrentlySelectedZoneName(String currentlySelectedZoneName) {
        this.currentlySelectedZoneName = currentlySelectedZoneName;
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Object>>> getObjectByZone(){
        if (place != null) {
            Zone selectedZone = getZoneByName(currentlySelectedZoneName);
            if (selectedZone != null) {
                selectedZone.setPlaceId(place.getId());
                return objectRepository.getObjects(selectedZone);
            } else {
                return new MutableLiveData<>();
            }
        } else {
            return new MutableLiveData<>();
        }
    }

    public Observer<RepositoryNotification<ArrayList<Zone>>> getZonesObserver = new Observer<RepositoryNotification<ArrayList<Zone>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Zone>> notification) {
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null) {
                    zoneNames.clear();
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
    private int i = 0;
    public Observer<RepositoryNotification<ArrayList<Object>>> getObjectsObserver = new Observer<RepositoryNotification<ArrayList<Object>>>() {
        @Override
        public void onChanged(RepositoryNotification<ArrayList<Object>> notification) {
            if (notification.getException() == null) {
                if (notification.getErrorMessage() == null) {
                    if(notification.getData().size() != 0){
                        Zone zone = getZoneById(notification.getData().get(0).getZoneId());
                        tempObjectsDataset.put(zone.getName(), NodeObject.getNodeObjectAll(notification.getData()));
                    }
                    i++;
                    if (i == zones.size()) {
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

    public MutableGraph<NodeObject> getGraphDataset() {
        return graphDataset;
    }

    private void setObjectsDatasetForNotification() {
        RepositoryNotification<HashMap<String, List<NodeObject>>> notification = new RepositoryNotification<>();
        notification.setData(tempObjectsDataset);
        objectsDataset.postValue(notification);
    }

    public void populateObjectsDataset() {
        if (place != null) {
            zoneRepository.getAllPlaceZones(place).observeForever(getZonesObserver);
        }
    }
}
