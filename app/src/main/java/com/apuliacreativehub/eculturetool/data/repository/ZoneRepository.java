package com.apuliacreativehub.eculturetool.data.repository;

import android.net.ConnectivityManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;
import com.apuliacreativehub.eculturetool.data.local.LocalZoneDAO;
import com.apuliacreativehub.eculturetool.data.network.zone.RemoteZoneDAO;
import com.apuliacreativehub.eculturetool.data.network.zone.ZoneRemoteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class ZoneRepository {
    private final RemoteZoneDAO remoteZoneDAO;
    private final LocalZoneDAO localZoneDAO;
    private final ConnectivityManager connectivityManager;
    private final Executor executor;

    public ZoneRepository(Executor executor, LocalDatabase localDatabase, ConnectivityManager connectivityManager) {
        remoteZoneDAO = ZoneRemoteDatabase.provideRemoteZoneDAO();
        localZoneDAO = localDatabase.zoneDAO();
        this.connectivityManager = connectivityManager;
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getAllPlaceZones(Place place) {
        MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            getResult = getAllPlaceZonesFromRemoteDatabase(place);
        } else {
            Log.d("SHOULDFETCH", "local");
            getResult = getAllPlaceZonesFromLocalDatabase(place);
        }

        return getResult;
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getAllPlaceZonesFromRemoteDatabase(Place place) {
        MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getResult = new MutableLiveData<>();
        Call<ArrayList<Zone>> call = remoteZoneDAO.getAllPlaceZones();
        executor.execute(() -> {
            try {
                Response<ArrayList<Zone>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<ArrayList<Zone>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    saveRemoteZonesToLocal(repositoryNotification.getData());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<ArrayList<Zone>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getAllPlaceZonesFromLocalDatabase(Place place) {
        MutableLiveData<RepositoryNotification<ArrayList<Zone>>> getResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<ArrayList<Zone>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setData((ArrayList<Zone>) localZoneDAO.getAllZonesByPlaceId(place.getId()));
                getResult.postValue(repositoryNotification);
            }
        });

        return getResult;
    }

    private void saveRemoteZonesToLocal(List<Zone> zones) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Zone place : zones) {
                    if (localZoneDAO.getZoneById(1) != null) localZoneDAO.insertZone(place);
                    else localZoneDAO.updateZone(place);
                }
            }
        });
    }
}
