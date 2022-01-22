package com.apuliacreativehub.eculturetool.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.network.place.PlaceRemoteDatabase;
import com.apuliacreativehub.eculturetool.data.network.place.RemotePlaceDAO;

import java.io.IOException;
import java.util.concurrent.Executor;

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
        Call<Void> call = remotePlaceDAO.AddPlace(place);
        MutableLiveData<RepositoryNotification<Void>> addResult = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                Response<Void> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                if(response.isSuccessful()){
                    repositoryNotification.setData(response.body());
                }else{
                    if(response.errorBody()!=null){
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
