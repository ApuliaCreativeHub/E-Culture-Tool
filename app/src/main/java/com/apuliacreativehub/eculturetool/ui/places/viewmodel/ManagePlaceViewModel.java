package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

import java.util.ArrayList;

public class ManagePlaceViewModel extends ZoneObjectViewModel {
    public ManagePlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getZonesFromDatabase() {
        if (super.getPlace() != null) {
            return zoneRepository.getAllPlaceZones(super.getPlace());
        } else {
            return new MutableLiveData<>();
        }
    }

    public MutableLiveData<RepositoryNotification<Zone>> addZoneToDatabase(String name) throws NoInternetConnectionException {
        if (super.getPlace() != null) {
            Zone zone = new Zone(name, super.getPlace().getId());
            return zoneRepository.addZone(zone);
        } else {
            return new MutableLiveData<>();
        }
    }

    public MutableLiveData<RepositoryNotification<Zone>> editZoneOnDatabase(String name) throws NoInternetConnectionException {
        if (super.getPlace() != null) {
            Zone zoneToEdit = getZoneByName(super.getCurrentlySelectedZoneName());
            if (zoneToEdit != null) {
                Zone zone = new Zone(zoneToEdit.getId(), name, super.getPlace().getId());
                return zoneRepository.editZone(zone);
            } else {
                return new MutableLiveData<>();
            }
        } else {
            return new MutableLiveData<>();
        }
    }

    public MutableLiveData<RepositoryNotification<Zone>> deleteZoneFromDatabase(String name) throws NoInternetConnectionException {
        if (super.getPlace() != null) {
            Zone zoneToDelete = getZoneByName(super.getCurrentlySelectedZoneName());
            if (zoneToDelete != null) {
                Zone zone = new Zone(zoneToDelete.getId(), name, super.getPlace().getId());
                return zoneRepository.deleteZone(zone);
            } else {
                return new MutableLiveData<>();
            }
        } else {
            return new MutableLiveData<>();
        }
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Object>>> getObjectByZone() {
        if (super.getPlace() != null) {
            Zone selectedZone = getZoneByName(super.getCurrentlySelectedZoneName());
            if (selectedZone != null) {
                selectedZone.setPlaceId(super.getPlace().getId());
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

    public Bundle getZonesBundle(){
        Bundle bundle = new Bundle();

        for (String zoneName : super.getZoneNames()) {
            bundle.putInt(zoneName, getZoneByName(zoneName).getId());
        }
        return bundle;
    }
}
