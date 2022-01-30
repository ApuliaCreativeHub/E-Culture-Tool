package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

public class CreatePlaceViewModel extends AbstractPlaceViewModel {

    public CreatePlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Place>> addPlace() {
        Place place;
        if (image.getScheme().equals(Utils.SCHEME_ANDROID_RESOURCE)) {
            place = new Place(name, address, description, image.toString());
        } else {
            place = new Place(name, address, description, "file://" + Utils.getRealPathFromURI(getApplication().getApplicationContext(), image));
        }
        Log.i("pathUri", place.getUriImg());
        return repository.addPlace(getApplication().getApplicationContext(), place);
    }

}