package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.ObjectRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.data.repository.ZoneRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import java.util.ArrayList;
import java.util.List;

public class CreatePathViewModel extends AndroidViewModel {
    private final ZoneRepository zoneRepository;
    private final ObjectRepository objectRepository;
    private List<Zone> zones;
    private List<String> zoneNames;
    private Place place;
    private String currentlySelectedZoneName = "";

    public CreatePathViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        zoneNames = new ArrayList<>();
        zoneRepository = new ZoneRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
        objectRepository = new ObjectRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
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

    public MutableLiveData<RepositoryNotification<Object>> getObjectById(int id) throws NoInternetConnectionException{
        return objectRepository.getObjectById(id);
    }
}
