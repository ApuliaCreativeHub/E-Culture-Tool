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

    public MutableLiveData<RepositoryNotification<Void>> addPlace(Context context, Place place) {
        MutableLiveData<RepositoryNotification<Void>> addResult = new MutableLiveData<>();
        try {
            InputStream image = context.getContentResolver().openInputStream(Uri.parse(place.getUriImg()));
            RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), image.read());
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), place.getName());
            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), place.getAddress());
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), place.getDescription());
            //TODO: failure picking external res
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
        } catch (FileNotFoundException e) {
            RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
            repositoryNotification.setException(e);
            addResult.postValue(repositoryNotification);
        } catch (IOException e) {
            RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
            repositoryNotification.setException(e);
            addResult.postValue(repositoryNotification);
        }
        return addResult;
    }
}
