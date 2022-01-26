package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

import java.util.ArrayList;

public class ShowPlacesViewModel extends AbstractPlaceViewModel{

    public ShowPlacesViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Place>>> getYourPlaces(){
        return repository.getYourPlaces();
    }
}
