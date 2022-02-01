package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.PathRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import java.util.ArrayList;
import java.util.List;

public class PlacePathsViewModel extends AndroidViewModel {
    private final PathRepository pathRepository;
    private Place place;
    private List<Path> paths;

    public PlacePathsViewModel(@NonNull Application application) {
        super(application);
        ECultureTool app = getApplication();
        paths = new ArrayList<>();
        pathRepository = new PathRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    public MutableLiveData<RepositoryNotification<List<Path>>> getPlacePathsFromDatabase() {
        if (place != null) {
            return pathRepository.getAllPlacePaths(place);
        } else {
            return new MutableLiveData<>();
        }
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
