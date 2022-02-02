package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

import java.util.ArrayList;
import java.util.List;

public class CreatePathViewModel extends CEPathViewModel {
    public CreatePathViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Path>> addPath() throws NoInternetConnectionException {
        Path path = new Path(super.getPathName());
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < super.getOrderedObjects().size(); i++) {
            int objectId = super.getOrderedObjects().get(i);
            objects.add(new Object(objectId));
        }
        path.setObjects(objects);
        return super.pathRepository.addPath(path);
    }
}
