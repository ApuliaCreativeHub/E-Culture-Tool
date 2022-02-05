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
import java.util.Collections;
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
        Call<ArrayList<Zone>> call = remoteZoneDAO.GetAllPlaceZones(place.getId());
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
                for (Zone zone : zones) {
                    if (localZoneDAO.getZoneById(zone.getId()) == null) {
                        localZoneDAO.insertZone(zone);
                    } else localZoneDAO.updateZone(zone);
                }
            }
        });
    }

    public MutableLiveData<RepositoryNotification<Zone>> addZone(Zone zone) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Zone>> addResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            addResult = addZoneToRemoteDatabase(zone);
        } else {
            throw new NoInternetConnectionException();
        }

        return addResult;
    }

    private MutableLiveData<RepositoryNotification<Zone>> addZoneToRemoteDatabase(Zone zone) {
        MutableLiveData<RepositoryNotification<Zone>> addResult = new MutableLiveData<>();
        Call<Zone> call = remoteZoneDAO.AddZone(zone);
        executor.execute(() -> {
            try {
                Response<Zone> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<Zone> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    // Add zone to local database too
                    saveRemoteZonesToLocal(Collections.singletonList(response.body()));
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                addResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<Zone> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                addResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return addResult;
    }

    public MutableLiveData<RepositoryNotification<Zone>> editZone(Zone zone) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Zone>> editResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            editResult = editZoneOnRemoteDatabase(zone);
        } else {
            throw new NoInternetConnectionException();
        }

        return editResult;
    }

    private MutableLiveData<RepositoryNotification<Zone>> editZoneOnRemoteDatabase(Zone zone) {
        MutableLiveData<RepositoryNotification<Zone>> editResult = new MutableLiveData<>();
        Call<Zone> call = remoteZoneDAO.EditZone(zone);
        executor.execute(() -> {
            try {
                Response<Zone> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<Zone> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    // Edit zone on local database too
                    saveRemoteZonesToLocal(Collections.singletonList(repositoryNotification.getData()));
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                editResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<Zone> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                editResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return editResult;
    }

    public MutableLiveData<RepositoryNotification<Zone>> deleteZone(Zone zone) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Zone>> deleteResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            deleteResult = deleteZoneFromRemoteDatabase(zone);
        } else {
            throw new NoInternetConnectionException();
        }

        return deleteResult;
    }

    private MutableLiveData<RepositoryNotification<Zone>> deleteZoneFromRemoteDatabase(Zone zone) {
        MutableLiveData<RepositoryNotification<Zone>> deleteResult = new MutableLiveData<>();
        Call<Void> call = remoteZoneDAO.DeleteZone(zone);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<Zone> repositoryNotification = new RepositoryNotification<>();
                try {
                    Response<Void> response = call.execute();
                    if (response.isSuccessful()) {
                        repositoryNotification.setData(zone);
                        // Delete zone from local database too
                        localZoneDAO.deleteZone(zone);
                    } else {
                        if (response.errorBody() != null) {
                            repositoryNotification.setErrorMessage(response.errorBody().string());
                        }
                    }
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                } catch (IOException ioe) {
                    repositoryNotification.setException(ioe);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
                deleteResult.postValue(repositoryNotification);
            }
        });
        return deleteResult;
    }
}
