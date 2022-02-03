package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.ObjectRepository;
import com.apuliacreativehub.eculturetool.data.repository.ZoneRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import java.util.ArrayList;
import java.util.List;

public class ZoneObjectViewModel extends AndroidViewModel {
    protected final ZoneRepository zoneRepository;
    protected final ObjectRepository objectRepository;
    private final List<String> zoneNames;
    private List<Zone> zones;
    private Place place;
    private String currentlySelectedZoneName = "";


    public ZoneObjectViewModel(@NonNull Application application) {
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

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;

        for(Zone zone : zones){
            zoneNames.add(zone.getName());
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
}
