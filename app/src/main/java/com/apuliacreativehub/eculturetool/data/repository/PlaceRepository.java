package com.apuliacreativehub.eculturetool.data.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.network.place.PlaceRemoteDatabase;
import com.apuliacreativehub.eculturetool.data.network.place.RemotePlaceDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class PlaceRepository {
    private final RemotePlaceDAO remotePlaceDAO;
    private final Executor executor;

    public PlaceRepository(Executor executor) {
        remotePlaceDAO = PlaceRemoteDatabase.provideRemotePlaceDAO();
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<Void>> addPlace(Place place) {
        MutableLiveData<RepositoryNotification<Void>> addResult = new MutableLiveData<>();
            RequestBody imgBody = RequestBody.create( "file://" + place.getUriImg(), MediaType.parse("image/*"));
            RequestBody name = RequestBody.create(place.getName(), MediaType.parse("text/plain"));
            RequestBody address = RequestBody.create( place.getAddress(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create( place.getDescription(), MediaType.parse("text/plain"));
            Call<Void> call = remotePlaceDAO.AddPlace(name, address, description, imgBody);
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
        return addResult;
    }
}
