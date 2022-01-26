package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.data.repository.ZoneRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import java.util.ArrayList;
import java.util.List;

public class ManagePlaceViewModel extends AndroidViewModel {
    private final ZoneRepository zoneRepository;
    private List<Zone> zones;
    private Place place;

    public ManagePlaceViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        zoneRepository = new ZoneRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
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

    public MutableLiveData<RepositoryNotification<Void>> addZonesToDatabase(String name) {
        if (place != null) {
            Zone zone = new Zone(name, place.getId());
            return zoneRepository.addZone(zone);
        } else {
            return new MutableLiveData<>();
        }
    }
}
