package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

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

public class ManagePlaceViewModel extends AndroidViewModel {
    private final ZoneRepository zoneRepository;
    private final ObjectRepository objectRepository;
    private List<Zone> zones;
    private List<String> zoneNames;
    private Place place;
    private String currentlySelectedZoneName = "";

    public ManagePlaceViewModel(Application application) {
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

    public MutableLiveData<RepositoryNotification<Zone>> addZoneToDatabase(String name) throws NoInternetConnectionException {
        if (place != null) {
            Zone zone = new Zone(name, place.getId());
            return zoneRepository.addZone(zone);
        } else {
            return new MutableLiveData<>();
        }
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public boolean removeZoneById(int id) {
        for (Zone zone : zones) {
            if (zone.getId() == id) return zones.remove(zone);
        }
        return false;
    }

    public List<String> getZoneNames() {
        return zoneNames;
    }

    public void setZoneNames(List<String> zoneNames) {
        this.zoneNames = zoneNames;
    }

    public MutableLiveData<RepositoryNotification<Zone>> editZoneOnDatabase(String name) throws NoInternetConnectionException {
        if (place != null) {
            Zone zoneToEdit = getZoneByName(currentlySelectedZoneName);
            if (zoneToEdit != null) {
                Zone zone = new Zone(zoneToEdit.getId(), name, place.getId());
                return zoneRepository.editZone(zone);
            } else {
                return new MutableLiveData<>();
            }
        } else {
            return new MutableLiveData<>();
        }
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

    public MutableLiveData<RepositoryNotification<Zone>> deleteZoneFromDatabase(String name) throws NoInternetConnectionException {
        if (place != null) {
            Zone zoneToDelete = getZoneByName(currentlySelectedZoneName);
            if (zoneToDelete != null) {
                Zone zone = new Zone(zoneToDelete.getId(), name, place.getId());
                return zoneRepository.deleteZone(zone);
            } else {
                return new MutableLiveData<>();
            }
        } else {
            return new MutableLiveData<>();
        }
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Object>>> getObjectByZone(){
        //return objectRepository.getObjects(getZoneByName(currentlySelectedZoneName), place);
        ArrayList<Object> stub = new ArrayList<>();
        stub.add(new Object("Name", "Description", "xd", 2));
        RepositoryNotification<ArrayList<Object>> stub1 = new RepositoryNotification<>();
        stub1.setData(stub);
        MutableLiveData<RepositoryNotification<ArrayList<Object>>> a = new MutableLiveData<>();
        a.postValue(stub1);
        return a;
    }
}
