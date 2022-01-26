package com.apuliacreativehub.eculturetool.data.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;
import com.apuliacreativehub.eculturetool.data.local.LocalPlaceDAO;
import com.apuliacreativehub.eculturetool.data.network.place.PlaceRemoteDatabase;
import com.apuliacreativehub.eculturetool.data.network.place.RemotePlaceDAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Response;

public class PlaceRepository {
    private final RemotePlaceDAO remotePlaceDAO;
    private final LocalPlaceDAO localPlaceDAO;
    private final Executor executor;
    private final ConnectivityManager connectivityManager;

    public PlaceRepository(Executor executor, LocalDatabase localDatabase, ConnectivityManager connectivityManager) {
        remotePlaceDAO = PlaceRemoteDatabase.provideRemotePlaceDAO();
        localPlaceDAO = localDatabase.placeDAO();
        this.connectivityManager = connectivityManager;
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<Void>> addPlace(Context context, Place place) {
        MutableLiveData<RepositoryNotification<Void>> addResult = new MutableLiveData<>();
        try {
            InputStream imgStream = context.getContentResolver().openInputStream(Uri.parse(place.getUriImg()));
            RequestBody imgBody = RequestBody.create(ByteString.read(imgStream, imgStream.available()), MediaType.parse("image/*"));
            MultipartBody.Part imgPart = MultipartBody.Part.createFormData("img", "img.png", imgBody);
            RequestBody name = RequestBody.create(place.getName(), MediaType.parse("text/plain"));
            RequestBody address = RequestBody.create(place.getAddress(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(place.getDescription(), MediaType.parse("text/plain"));
            Call<Void> call = remotePlaceDAO.addPlace(name, address, description, imgPart);
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

    public MutableLiveData<RepositoryNotification<Void>> deletePlace(Place place) {
        MutableLiveData<RepositoryNotification<Void>> deleteResult = new MutableLiveData<>();
        Call<Void> call = remotePlaceDAO.DeletePlace(place);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                try {
                    Response<Void> response = call.execute();
                    if (response.isSuccessful()) {
                        repositoryNotification.setData(response.body());
                        localPlaceDAO.deletePlace(place);
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

    public MutableLiveData<RepositoryNotification<Void>> editPlace(Context context, Place place){
        MutableLiveData<RepositoryNotification<Void>> editResult = new MutableLiveData<>();
        try {
            Call<Void> call;
            RequestBody id = RequestBody.create(String.valueOf(place.getId()), MediaType.parse("text/plain"));
            RequestBody name = RequestBody.create(place.getName(), MediaType.parse("text/plain"));
            RequestBody address = RequestBody.create(place.getAddress(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(place.getDescription(), MediaType.parse("text/plain"));
            if(place.getUriImg() != null){
                InputStream imgStream = context.getContentResolver().openInputStream(Uri.parse(place.getUriImg()));
                RequestBody imgBody = RequestBody.create(ByteString.read(imgStream, imgStream.available()), MediaType.parse("image/*"));
                MultipartBody.Part imgPart = MultipartBody.Part.createFormData("img", "img.png", imgBody);
                call = remotePlaceDAO.EditPlace(id, name, address, description, imgPart);
            }else {
                call = remotePlaceDAO.EditPlaceNoImg(id, name, address, description);
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

    public MutableLiveData<RepositoryNotification<ArrayList<Place>>> getYourPlaces() {
        MutableLiveData<RepositoryNotification<ArrayList<Place>>> getResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            getResult = getYourPlacesFromRemoteDatabase();
        } else {
            Log.d("SHOULDFETCH", "local");
            getResult = getAllPlacesFromLocalDatabase();
        }

        return getResult;
    }

    private void saveRemotePlacesToLocal(List<Place> places) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Place place : places) {
                    if (localPlaceDAO.getPlaceById(place.getId()) == null)
                        localPlaceDAO.insertPlace(place);
                    else localPlaceDAO.updatePlace(place);
                }
            }
        });
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Place>>> getYourPlacesFromRemoteDatabase() {
        MutableLiveData<RepositoryNotification<ArrayList<Place>>> getResult = new MutableLiveData<>();
        Call<ArrayList<Place>> call = remotePlaceDAO.getYourPlaces();
        executor.execute(() -> {
            try {
                Response<ArrayList<Place>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<ArrayList<Place>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                    saveRemotePlacesToLocal(repositoryNotification.getData());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<ArrayList<Place>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Place>>> getAllPlacesFromLocalDatabase() {
        MutableLiveData<RepositoryNotification<ArrayList<Place>>> getResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RepositoryNotification<ArrayList<Place>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setData((ArrayList<Place>) localPlaceDAO.getAllPlaces());
                getResult.postValue(repositoryNotification);
            }
        });

        return getResult;
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Place>>> getAllPlaces() {
        MutableLiveData<RepositoryNotification<ArrayList<Place>>> getResult;
        if (RepositoryUtils.shouldFetch(connectivityManager) == RepositoryUtils.FROM_REMOTE_DATABASE) {
            Log.d("SHOULDFETCH", "remote");
            getResult = getAllPlacesFromRemoteDatabase();
        } else {
            Log.d("SHOULDFETCH", "local");
            getResult = getAllPlacesFromLocalDatabase();
        }

        return getResult;
    }

    private MutableLiveData<RepositoryNotification<ArrayList<Place>>> getAllPlacesFromRemoteDatabase() {
        MutableLiveData<RepositoryNotification<ArrayList<Place>>> getResult = new MutableLiveData<>();
        Call<ArrayList<Place>> call = remotePlaceDAO.getAllPlaces();
        executor.execute(() -> {
            try {
                Response<ArrayList<Place>> response = call.execute();
                Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                RepositoryNotification<ArrayList<Place>> repositoryNotification = new RepositoryNotification<>();
                if (response.isSuccessful()) {
                    repositoryNotification.setData(response.body());
                } else {
                    if (response.errorBody() != null) {
                        repositoryNotification.setErrorMessage(response.errorBody().string());
                    }
                }
                getResult.postValue(repositoryNotification);
            } catch (IOException ioe) {
                RepositoryNotification<ArrayList<Place>> repositoryNotification = new RepositoryNotification<>();
                repositoryNotification.setException(ioe);
                getResult.postValue(repositoryNotification);
                Log.e("RETROFITERROR", ioe.getMessage());
            }
        });
        return getResult;
    }
}
