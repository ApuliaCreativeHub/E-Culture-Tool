package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

public class CreatePlaceViewModel extends AbstractPlaceViewModel {

    public CreatePlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> addPlace(){
        Place place = new Place(name, address, description, Utils.getRealPathFromURI(getApplication().getApplicationContext(), image));
        Log.i("pathUri", place.getUriImg());
        return repository.addPlace(getApplication().getApplicationContext(), place);
    }

}