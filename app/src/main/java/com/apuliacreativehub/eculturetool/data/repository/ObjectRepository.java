package com.apuliacreativehub.eculturetool.data.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;
import com.apuliacreativehub.eculturetool.data.local.LocalObjectDAO;
import com.apuliacreativehub.eculturetool.data.network.object.ObjectRemoteDatabase;
import com.apuliacreativehub.eculturetool.data.network.object.RemoteObjectDAO;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Response;

public class ObjectRepository {
    private final RemoteObjectDAO remoteObjectDAO;
    private final LocalObjectDAO localObjectDAO;
    private final Executor executor;
    private final ConnectivityManager connectivityManager;

    public ObjectRepository(Executor executor, LocalDatabase localDatabase, ConnectivityManager connectivityManager) {
        remoteObjectDAO = ObjectRemoteDatabase.provideRemoteObjectDAO();
        localObjectDAO = localDatabase.objectDAO();
        this.connectivityManager = connectivityManager;
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<Void>> addObject(Context context, Object object) {
        MutableLiveData<RepositoryNotification<Void>> addResult = new MutableLiveData<>();
        try {
            InputStream imgStream = context.getContentResolver().openInputStream(Uri.parse(object.getUriImg()));
            RequestBody imgBody = RequestBody.create(ByteString.read(imgStream, imgStream.available()), MediaType.parse("image/*"));
            MultipartBody.Part imgPart = MultipartBody.Part.createFormData("img", "img.png", imgBody);
            RequestBody name = RequestBody.create(object.getName(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(object.getDescription(), MediaType.parse("text/plain"));
            RequestBody zoneId = RequestBody.create(String.valueOf(object.getZoneId()), MediaType.parse("text/plain"));
            Call<Void> call = remoteObjectDAO.AddObject(name, description, zoneId, imgPart);
            executor.execute(() -> {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                    if (response.isSuccessful()) {
                        repositoryNotification.setData(response.body());
                    } else {
                        if (response.errorBody() != null) {
                            repositoryNotification.setErrorMessage(response.errorBody().string());
                        }
                    }
                    addResult.postValue(repositoryNotification);
                } catch (IOException ioe) {
                    RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setException(ioe);
                    addResult.postValue(repositoryNotification);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            });
        } catch (IOException ioe) {
            RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
            repositoryNotification.setException(ioe);
            addResult.postValue(repositoryNotification);
            Log.e("RETROFITERROR", ioe.getMessage());
        }
        return addResult;
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Object>>> getObjects(Zone zone) {
        MutableLiveData<RepositoryNotification<ArrayList<Object>>> getResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            getResult = getObjectsFromRemoteDatabase(zone);
        } else {
            Log.d("SHOULDFETCH", "remote");
            getResult = getObjectsFromLocalDatabase(zone);
        }

        return getResult;
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Object>>> getObjectsFromRemoteDatabase(Zone zone) {
        MutableLiveData<RepositoryNotification<ArrayList<Object>>> getResult = new MutableLiveData<>();
        Call<ArrayList<Object>> call = remoteObjectDAO.GetObjectByZoneAndPlace(zone.getId());
        executor.execute(() -> {
            try {
                Response<ArrayList<Object>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<ArrayList<Object>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    saveRemoteObjectsToLocal(repositoryNotification.getData());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<ArrayList<Object>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Object>>> getObjectsFromLocalDatabase(Zone zone) {
        MutableLiveData<RepositoryNotification<ArrayList<Object>>> getResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<ArrayList<Object>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setData((ArrayList<Object>) localObjectDAO.getAllObjectsByZoneId(zone.getId()));
                getResult.postValue(repositoryNotification);
            }
        });

        return getResult;
    }

    private void saveRemoteObjectsToLocal(List<Object> objects) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Object object : objects) {
                    if (localObjectDAO.getObjectById(object.getId()) != null)
                        localObjectDAO.insertObject(object);
                    else localObjectDAO.updateObject(object);
                }
            }
        });
    }

    public MutableLiveData<RepositoryNotification<Void>> editObject(Context context, Object object) throws NoInternetConnectionException {
        MutableLiveData<RepositoryNotification<Void>> editResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            editResult = editObjectOnRemoteDatabase(context, object);
        } else {
            throw new NoInternetConnectionException();
        }

        return editResult;
    }

    private MutableLiveData<RepositoryNotification<Void>> editObjectOnRemoteDatabase(Context context, Object object) {
        MutableLiveData<RepositoryNotification<Void>> editResult = new MutableLiveData<>();
        try {
            RequestBody id = RequestBody.create(String.valueOf(object.getId()), MediaType.parse("text/plain"));
            RequestBody name = RequestBody.create(object.getName(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(object.getDescription(), MediaType.parse("text/plain"));
            RequestBody zoneId = RequestBody.create(String.valueOf(object.getZoneId()), MediaType.parse("text/plain"));
            Call<Void> call;
            if(object.getUriImg() != null){
                InputStream imgStream = context.getContentResolver().openInputStream(Uri.parse(object.getUriImg()));
                RequestBody imgBody = RequestBody.create(ByteString.read(imgStream, imgStream.available()), MediaType.parse("image/*"));
                MultipartBody.Part imgPart = MultipartBody.Part.createFormData("img", "img.png", imgBody);
                call = remoteObjectDAO.EditObject(id, name, description, zoneId, imgPart);
            }else {
                call = remoteObjectDAO.EditObjectNoImg(id, name, description, zoneId);
            }
            executor.execute(() -> {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                    if (response.isSuccessful()) {
                        repositoryNotification.setData(response.body());
                    } else {
                        if (response.errorBody() != null) {
                            repositoryNotification.setErrorMessage(response.errorBody().string());
                        }
                    }
                    editResult.postValue(repositoryNotification);
                } catch (IOException ioe) {
                    RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setException(ioe);
                    editResult.postValue(repositoryNotification);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            });
        } catch (IOException ioe) {
            RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
            repositoryNotification.setException(ioe);
            editResult.postValue(repositoryNotification);
            Log.e("RETROFITERROR", ioe.getMessage());
        }
            return editResult;
    }
}
