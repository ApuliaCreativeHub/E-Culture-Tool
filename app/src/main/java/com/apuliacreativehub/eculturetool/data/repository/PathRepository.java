package com.apuliacreativehub.eculturetool.data.repository;

import android.net.ConnectivityManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.UserPreferencesManager;
import com.apuliacreativehub.eculturetool.data.entity.IsPresentIn;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.VisitorIsPresentIn;
import com.apuliacreativehub.eculturetool.data.entity.VisitorPath;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;
import com.apuliacreativehub.eculturetool.data.local.LocalIsPresentInDAO;
import com.apuliacreativehub.eculturetool.data.local.LocalObjectDAO;
import com.apuliacreativehub.eculturetool.data.local.LocalPathDAO;
import com.apuliacreativehub.eculturetool.data.local.LocalPlaceDAO;
import com.apuliacreativehub.eculturetool.data.local.LocalZoneDAO;
import com.apuliacreativehub.eculturetool.data.local.VisitorIsPresentInDAO;
import com.apuliacreativehub.eculturetool.data.local.VisitorPathDAO;
import com.apuliacreativehub.eculturetool.data.network.path.RemotePathDAO;
import com.apuliacreativehub.eculturetool.data.network.path.RemotePathDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class PathRepository {
    private final RemotePathDAO remotePathDAO;
    private final LocalPathDAO localPathDAO;
    private final LocalObjectDAO localObjectDAO;
    private final LocalPlaceDAO localPlaceDAO;
    private final LocalZoneDAO localZoneDAO;
    private final VisitorPathDAO visitorPathDAO;
    private final VisitorIsPresentInDAO visitorIsPresentInDAO;
    private final LocalIsPresentInDAO localIsPresentInDAO;
    private final ConnectivityManager connectivityManager;
    private final Executor executor;

    public PathRepository(Executor executor, LocalDatabase localDatabase, ConnectivityManager connectivityManager) {
        remotePathDAO = RemotePathDatabase.provideRemotePathDAO();
        localPathDAO = localDatabase.pathDAO();
        localIsPresentInDAO = localDatabase.isPresentInDAO();
        localObjectDAO = localDatabase.objectDAO();
        localPlaceDAO = localDatabase.placeDAO();
        localZoneDAO = localDatabase.zoneDAO();
        visitorPathDAO = localDatabase.visitorPathDAO();
        visitorIsPresentInDAO = localDatabase.visitorIsPresentInDAO();
        this.connectivityManager = connectivityManager;
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<List<Path>>> getAllCuratorPlacePaths(Place place) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<List<Path>>> getResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            getResult = getAllCuratorPlacePathsFromRemoteDatabase(place);
        } else {
            throw new NoInternetConnectionException();
        }

        return getResult;
    }

    private MutableLiveData<RepositoryNotification<List<Path>>> getAllCuratorPlacePathsFromRemoteDatabase(Place place) {
        MutableLiveData<RepositoryNotification<List<Path>>> getResult = new MutableLiveData<>();
        Call<List<Path>> call = remotePathDAO.getCuratorPlacePaths(place.getId());
        executor.execute(() -> {
            try {
                Response<List<Path>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<List<Path>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    //saveRemotePathsToLocal(repositoryNotification.getData());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<List<Path>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }

    public MutableLiveData<RepositoryNotification<List<Path>>> getYourPaths() {
        MutableLiveData<RepositoryNotification<List<Path>>> getResult;
        if (UserPreferencesManager.getToken().equals("")) {
            getResult = getVisitorPathsFromLocalDatabase();
        } else if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            getResult = getYourPathsFromRemoteDatabase();
        } else {
            Log.d("SHOULDFETCH", "remote");
            getResult = getYourPathsFromLocalDatabase();
        }

        return getResult;
    }

    private MutableLiveData<RepositoryNotification<List<Path>>> getVisitorPathsFromLocalDatabase() {
        MutableLiveData<RepositoryNotification<List<Path>>> getResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<List<Path>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setData(getVisitorPathsWithObjects());
                getResult.postValue(repositoryNotification);
            }
        });

        return getResult;
    }

    private List<Path> getVisitorPathsWithObjects() {
        List<VisitorPath> paths = visitorPathDAO.getAllYourPaths();
        List<Path> convertedPaths = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).setPlace(localPlaceDAO.getPlaceByVisitorPathId(paths.get(i).getVisitorPathId()));
            List<Object> objects = localObjectDAO.getObjectsByVisitorPathId(paths.get(i).getVisitorPathId());
            for (int j = 0; j < objects.size(); j++) {
                Zone zone = localZoneDAO.getZoneById(objects.get(j).getZoneId());
                objects.get(j).setZone(zone);
            }
            paths.get(i).setObjects(objects);
            convertedPaths.add(new Path(paths.get(i)));
        }

        return convertedPaths;
    }

    private MutableLiveData<RepositoryNotification<List<Path>>> getYourPathsFromRemoteDatabase() {
        MutableLiveData<RepositoryNotification<List<Path>>> getResult = new MutableLiveData<>();
        Call<List<Path>> call = remotePathDAO.getYourPaths();
        executor.execute(() -> {
            try {
                Response<List<Path>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<List<Path>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    saveRemotePlaceToLocal(repositoryNotification.getData());
                    saveRemotePathsToLocal(repositoryNotification.getData());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<List<Path>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }

    private MutableLiveData<RepositoryNotification<List<Path>>> getYourPathsFromLocalDatabase() {
        MutableLiveData<RepositoryNotification<List<Path>>> getResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<List<Path>> repositoryNotification = new RepositoryNotification<>();
                List<Path> paths = localPathDAO.getAllYourPaths();
                for (int i = 0; i < paths.size(); i++) {
                    paths.get(i).setPlace(localPlaceDAO.getPlaceByPathId(paths.get(i).getId()));
                    paths.get(i).setObjects(localObjectDAO.getObjectsByPathId(paths.get(i).getId()));
                }
                repositoryNotification.setData(paths);
                getResult.postValue(repositoryNotification);
            }
        });

        return getResult;
    }

    public MutableLiveData<RepositoryNotification<Path>> addPath(Path path) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Path>> addResult;
        if (UserPreferencesManager.getToken().equals("")) {
            addResult = addVisitorPathToLocalDatabase(path);
        } else if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            addResult = addPathToRemoteDatabase(path);
        } else {
            throw new NoInternetConnectionException();
        }

        return addResult;
    }

    private MutableLiveData<RepositoryNotification<Path>> addPathToRemoteDatabase(Path path) {
        MutableLiveData<RepositoryNotification<Path>> addResult = new MutableLiveData<>();
        Call<Path> call = remotePathDAO.addPath(path);
        executor.execute(() -> {
            try {
                Response<Path> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    // Add path to local database too
                    saveRemotePathsToLocal(Collections.singletonList(response.body()));
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                addResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                addResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return addResult;
    }

    private MutableLiveData<RepositoryNotification<Path>> addVisitorPathToLocalDatabase(Path path) {
        MutableLiveData<RepositoryNotification<Path>> addResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                saveRemotePlaceToLocal(Collections.singletonList(path));
                saveVisitorPath(Collections.singletonList(path));
                repositoryNotification.setData(path);
                addResult.postValue(repositoryNotification);
            }
        });
        return addResult;
    }

    private void saveVisitorPath(List<Path> paths) {
        for (Path path : paths) {
            VisitorPath visitorPath = new VisitorPath(path);
            long id;
            if (visitorPathDAO.getPathById(visitorPath.getVisitorPathId()) == null)
                id = visitorPathDAO.insertPath(visitorPath);
            else {
                visitorPathDAO.updatePath(visitorPath);
                visitorIsPresentInDAO.deleteRelationsByPathId(visitorPath.getVisitorPathId());
                id = visitorPath.getVisitorPathId();
            }
            int i = 1;
            for (Object object : visitorPath.getObjects()) {
                Zone zone;
                if (object.getZone() == null) zone = new Zone(object.getZoneId());
                else zone = object.getZone();
                if (path.getPlace() != null)
                    zone.setPlaceId(path.getPlace().getId());
                if (localZoneDAO.getZoneById(object.getZoneId()) == null) {
                    localZoneDAO.insertZone(zone);
                } else {
                    localZoneDAO.updateZone(zone);
                }
                if (localObjectDAO.getObjectById(object.getId()) == null) {
                    localObjectDAO.insertObject(object);
                } else {
                    localObjectDAO.updateObject(object);
                }
                visitorIsPresentInDAO.insertRelation(new VisitorIsPresentIn(object.getId(), (int) id, i));
                i++;
            }
        }
    }

    public MutableLiveData<RepositoryNotification<Path>> editPath(Path path) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Path>> editResult;
        if (UserPreferencesManager.getToken().equals("")) {
            editResult = editVisitorPathOnLocalDatabase(path);
        } else if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            editResult = editPathOnRemoteDatabase(path);
        } else {
            throw new NoInternetConnectionException();
        }

        return editResult;
    }

    private MutableLiveData<RepositoryNotification<Path>> editVisitorPathOnLocalDatabase(Path path) {
        MutableLiveData<RepositoryNotification<Path>> editResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                saveVisitorPath(Collections.singletonList(path));
                repositoryNotification.setData(path);
                editResult.postValue(repositoryNotification);
            }
        });

        return editResult;
    }

    private MutableLiveData<RepositoryNotification<Path>> editPathOnRemoteDatabase(Path path) {
        MutableLiveData<RepositoryNotification<Path>> editResult = new MutableLiveData<>();
        Call<Path> call = remotePathDAO.editPath(path);
        executor.execute(() -> {
            try {
                Response<Path> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(path);
                    // Edit path on local database too
                    saveRemotePathsToLocal(Collections.singletonList(path));
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                editResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                editResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return editResult;
    }

    public MutableLiveData<RepositoryNotification<Path>> deletePath(Path path) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Path>> deleteResult;
        if (UserPreferencesManager.getToken().equals("")) {
            deleteResult = deleteVisitorPathOnLocalDatabase(path);
        } else if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            deleteResult = deletePathFromRemoteDatabase(path);
        } else {
            throw new NoInternetConnectionException();
        }

        return deleteResult;
    }

    private MutableLiveData<RepositoryNotification<Path>> deleteVisitorPathOnLocalDatabase(Path path) {
        MutableLiveData<RepositoryNotification<Path>> deleteResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                VisitorPath visitorPath = new VisitorPath(path);
                visitorIsPresentInDAO.deleteRelationsByPathId(visitorPath.getVisitorPathId());
                visitorPathDAO.deletePath(visitorPath);
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setData(path);
                deleteResult.postValue(repositoryNotification);
            }
        });

        return deleteResult;
    }

    private MutableLiveData<RepositoryNotification<Path>> deletePathFromRemoteDatabase(Path path) {
        MutableLiveData<RepositoryNotification<Path>> deleteResult = new MutableLiveData<>();
        Call<Void> call = remotePathDAO.deletePath(path);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<Path> repositoryNotification = new RepositoryNotification<>();
                try {
                    Response<Void> response = call.execute();
                    if (response.isSuccessful()) {
                        repositoryNotification.setData(path);
                        // Delete path from local database too
                        localIsPresentInDAO.deleteRelationsByPathId(path.getId());
                        localPathDAO.deletePath(path);
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

    private void saveRemotePathsToLocal(List<Path> paths) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Path path : paths) {
                    if (localPathDAO.getPathById(path.getId()) == null)
                        localPathDAO.insertPath(path);
                    else localPathDAO.updatePath(path);
                    localIsPresentInDAO.deleteRelationsByPathId(path.getId());
                    int i = 1;
                    for (Object object : path.getObjects()) {
                        Zone zone;
                        if (object.getZone() == null) zone = new Zone(object.getZoneId());
                        else zone = object.getZone();
                        if (path.getPlace() != null)
                            zone.setPlaceId(path.getPlace().getId());
                        if (localZoneDAO.getZoneById(object.getZoneId()) == null) {
                            localZoneDAO.insertZone(zone);
                        } else {
                            localZoneDAO.updateZone(zone);
                        }
                        if (localObjectDAO.getObjectById(object.getId()) == null) {
                            localObjectDAO.insertObject(object);
                        } else {
                            localObjectDAO.updateObject(object);
                        }
                        localIsPresentInDAO.insertRelation(new IsPresentIn(object.getId(), path.getId(), i));
                        i++;
                    }
                }
            }
        });
    }

    private void saveRemotePlaceToLocal(List<Path> paths) {
        for (Path path : paths) {
            if (localPlaceDAO.getPlaceById(path.getPlace().getId()) == null) {
                localPlaceDAO.insertPlace(path.getPlace());
            } else {
                localPlaceDAO.updatePlace(path.getPlace());
            }
        }
    }

    public MutableLiveData<Integer> countVisitorPaths() {
        MutableLiveData<Integer> isDataPresent = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                isDataPresent.postValue(visitorPathDAO.countPaths());
            }
        });

        return isDataPresent;
    }

    public void saveVisitorPathsToRemoteDatabase() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Path> paths = getVisitorPathsWithObjects();
                for (Path path : paths) {
                    VisitorPath visitorPath = new VisitorPath(path);
                    Call<Path> call = remotePathDAO.addPath(path);
                    executor.execute(() -> {
                        try {
                            Response<Path> response = call.execute();
                            Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                            if (response.isSuccessful()) {
                                // Add path to local database too
                                saveRemotePathsToLocal(Collections.singletonList(response.body()));
                                visitorIsPresentInDAO.deleteRelationsByPathId(visitorPath.getVisitorPathId());
                                visitorPathDAO.deletePath(visitorPath);
                            } else {
                                if (response.errorBody() != null) {
                                    Log.e("RETROFITERROR", response.errorBody().string());
                                }
                            }
                        } catch (IOException ioe) {
                            Log.e("RETROFITERROR", ioe.getMessage());
                        }
                    });
                }
            }
        });
    }
}
