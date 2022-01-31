package com.apuliacreativehub.eculturetool.data.repository;

import android.net.ConnectivityManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.PathWithObjects;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;
import com.apuliacreativehub.eculturetool.data.local.LocalPathDAO;
import com.apuliacreativehub.eculturetool.data.network.path.RemotePathDAO;
import com.apuliacreativehub.eculturetool.data.network.path.RemotePathDatabase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class PathRepository {
    private final RemotePathDAO remotePathDAO;
    private final LocalPathDAO localPathDAO;
    private final ConnectivityManager connectivityManager;
    private final Executor executor;

    public PathRepository(Executor executor, LocalDatabase localDatabase, ConnectivityManager connectivityManager) {
        remotePathDAO = RemotePathDatabase.provideRemotePathDAO();
        localPathDAO = localDatabase.pathDAO();
        this.connectivityManager = connectivityManager;
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<List<PathWithObjects>>> getAllPlacePaths(Place place) {
        MutableLiveData<RepositoryNotification<List<PathWithObjects>>> getResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            getResult = getAllPlacePathsFromRemoteDatabase(place);
        } else {
            Log.d("SHOULDFETCH", "local");
            getResult = getAllPlacePathsFromLocalDatabase(place);
        }

        return getResult;
    }

    private MutableLiveData<RepositoryNotification<List<PathWithObjects>>> getAllPlacePathsFromRemoteDatabase(Place place) {
        MutableLiveData<RepositoryNotification<List<PathWithObjects>>> getResult = new MutableLiveData<>();
        Call<List<PathWithObjects>> call = remotePathDAO.getPlacePaths(place.getId());
        executor.execute(() -> {
            try {
                Response<List<PathWithObjects>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<List<PathWithObjects>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    saveRemotePathsToLocal(repositoryNotification.getData());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<List<PathWithObjects>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }

    private MutableLiveData<RepositoryNotification<List<PathWithObjects>>> getAllPlacePathsFromLocalDatabase(Place place) {
        MutableLiveData<RepositoryNotification<List<PathWithObjects>>> getResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<List<PathWithObjects>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setData(localPathDAO.getAllPathsByPlaceId(place.getId()));
                getResult.postValue(repositoryNotification);
            }
        });

        return getResult;
    }

    private void saveRemotePathsToLocal(List<PathWithObjects> paths) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (PathWithObjects path : paths) {
                    if (localPathDAO.getPathById(path.path.getId()) == null) {
                        localPathDAO.insertPath(path);
                    } else localPathDAO.updatePath(path);
                }
            }
        });
    }

    public MutableLiveData<RepositoryNotification<PathWithObjects>> addPath(PathWithObjects path) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<PathWithObjects>> addResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            addResult = addPathToRemoteDatabase(path);
        } else {
            throw new NoInternetConnectionException();
        }

        return addResult;
    }

    private MutableLiveData<RepositoryNotification<PathWithObjects>> addPathToRemoteDatabase(PathWithObjects path) {
        MutableLiveData<RepositoryNotification<PathWithObjects>> addResult = new MutableLiveData<>();
        Call<PathWithObjects> call = remotePathDAO.addPath(path);
        executor.execute(() -> {
            try {
                Response<PathWithObjects> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<PathWithObjects> repositoryNotification = new RepositoryNotification<>();
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
                RepositoryNotification<PathWithObjects> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                addResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return addResult;
    }

    public MutableLiveData<RepositoryNotification<PathWithObjects>> editPath(PathWithObjects path) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<PathWithObjects>> editResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            editResult = editPathOnRemoteDatabase(path);
        } else {
            throw new NoInternetConnectionException();
        }

        return editResult;
    }

    private MutableLiveData<RepositoryNotification<PathWithObjects>> editPathOnRemoteDatabase(PathWithObjects path) {
        MutableLiveData<RepositoryNotification<PathWithObjects>> editResult = new MutableLiveData<>();
        Call<PathWithObjects> call = remotePathDAO.editPath(path);
        executor.execute(() -> {
            try {
                Response<PathWithObjects> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<PathWithObjects> repositoryNotification = new RepositoryNotification<>();
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
                RepositoryNotification<PathWithObjects> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                editResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return editResult;
    }

    public MutableLiveData<RepositoryNotification<Path>> deletePath(Path path) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Path>> deleteResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            deleteResult = deletePathFromRemoteDatabase(path);
        } else {
            throw new NoInternetConnectionException();
        }

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
}
