package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

public class EditPlaceViewModel extends AbstractPlaceViewModel {

    public EditPlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> deletePlace(Place place) {
        return repository.deletePlace(place);
    }

}