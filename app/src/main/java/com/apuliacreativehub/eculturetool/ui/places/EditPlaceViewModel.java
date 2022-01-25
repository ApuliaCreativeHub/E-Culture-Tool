package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

public class EditPlaceViewModel extends AbstractPlaceViewModel {

    public EditPlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> deletePlace(Place place) {
        return repository.deletePlace(place);
    }

    public MutableLiveData<RepositoryNotification<Void>> editPlace(Place place) {
        return repository.editPlace(getApplication().getApplicationContext(), place);
    }

}