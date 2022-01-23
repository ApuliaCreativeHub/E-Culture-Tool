package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

public class CreatePlaceViewModel extends AbstractPlaceViewModel {

    public CreatePlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> addPlace(){
        Place place = new Place(name, address, description, image.getPath());
        return repository.addPlace(getApplication().getApplicationContext(), place);
    }
}