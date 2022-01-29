package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Zone;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Utils;

public class EditObjectViewModel extends AbstractObjectViewModel {
    private int objectID;

    public EditObjectViewModel(Application application) {
        super(application);
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public MutableLiveData<RepositoryNotification<Object>> editObject() throws NoInternetConnectionException {
        Object object;
        if(image != null){
            if (image.getScheme().equals(Utils.SCHEME_ANDROID_RESOURCE)) {
                object = new Object(objectID, name, description, image.toString(), zoneID);
            } else {
                object = new Object(objectID, name, description, "file://" + Utils.getRealPathFromURI(getApplication().getApplicationContext(), image), zoneID);
            }
        }else{
            object = new Object(objectID, name, description, zoneID);
        }
        return repository.editObject(getApplication().getApplicationContext(), object);
    }

    public MutableLiveData<RepositoryNotification<Void>> deleteObject() throws NoInternetConnectionException {
        if (objectID != 0) {
            Object object = new Object(objectID);
            return repository.deleteObject(object);
        } else {
            return new MutableLiveData<>();
        }
    }
}