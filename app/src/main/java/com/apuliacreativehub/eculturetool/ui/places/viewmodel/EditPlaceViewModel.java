package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

public class EditPlaceViewModel extends AbstractPlaceViewModel {

    public EditPlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> deletePlace() {
        Place place;
        place = new Place(id, name, address, description, "");
        return repository.deletePlace(place);
    }

    public MutableLiveData<RepositoryNotification<Void>> editPlace() {
        Place place;
        if(image != null){
            if (image.getScheme().equals(Utils.SCHEME_ANDROID_RESOURCE)) {
                place = new Place(id, name, address, description, image.toString());
            } else {
                place = new Place(id, name, address, description, "file://" + Utils.getRealPathFromURI(getApplication().getApplicationContext(), image));
            }
        }else{
            place = new Place(id, name, address, description, null);
        }
        return repository.editPlace(getApplication().getApplicationContext(), place);
    }

}