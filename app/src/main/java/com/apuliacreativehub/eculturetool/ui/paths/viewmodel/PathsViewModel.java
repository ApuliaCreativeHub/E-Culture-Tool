package com.apuliacreativehub.eculturetool.ui.paths.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.PathRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import java.util.List;

public class PathsViewModel extends AndroidViewModel {
    private final PathRepository pathRepository;
    private List<Path> paths;
    private int pathIdToRemove;

    public PathsViewModel(@NonNull Application application) {
        super(application);
        ECultureTool app = (ECultureTool) application;
        pathRepository = new PathRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    public void setPathIdToRemove(int pathIdToRemove) {
        this.pathIdToRemove = pathIdToRemove;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public void removePathFromList(int id){
        paths.removeIf(path -> path.getId() == id);
    }

    public MutableLiveData<RepositoryNotification<List<Path>>> getYourPaths(){
        return pathRepository.getYourPaths();
    }

    public MutableLiveData<RepositoryNotification<Path>> deletePath() throws NoInternetConnectionException {
        if(pathIdToRemove != 0){
            return pathRepository.deletePath(new Path(pathIdToRemove));
        }
        return new MutableLiveData<>();
    }
}
