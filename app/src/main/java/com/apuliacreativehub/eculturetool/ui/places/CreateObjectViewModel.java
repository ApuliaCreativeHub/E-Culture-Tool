package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

public class CreateObjectViewModel extends AbstractObjectViewModel {

    public CreateObjectViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Object>> addObject(){
        Object object;
        if (image.getScheme().equals(Utils.SCHEME_ANDROID_RESOURCE)) {
            object = new Object(name, description, image.toString(), zoneID);
        } else {
            object = new Object(name, description, "file://" + Utils.getRealPathFromURI(getApplication().getApplicationContext(), image), zoneID);
        }
        Log.i("objectPathUri", object.getUriImg());
        return repository.addObject(getApplication().getApplicationContext(), object);
    }

}