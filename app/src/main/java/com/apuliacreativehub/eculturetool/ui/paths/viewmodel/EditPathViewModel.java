package com.apuliacreativehub.eculturetool.ui.paths.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CEPathViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditPathViewModel extends CEPathViewModel {
    private Path path;

    public EditPathViewModel(@NonNull Application application) {
        super(application);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setGraphDatasetFromOrderedObjects() {
        if (path != null) {
            for (Object object : path.getObjects()) {
                NodeObject datasetNodeObject = getNodeObjectFromObjectsDatasetById(object.getId());
                if (datasetNodeObject != null) {
                    datasetNodeObject.check();
                }
                increaseGraph(datasetNodeObject);
            }
        }
    }

    public NodeObject getNodeObjectFromObjectsDatasetById(int objectId) {
        // TODO: Find object in objectsDataset by id
        return new NodeObject(new Object());
    }

    public MutableLiveData<RepositoryNotification<Path>> editPath() throws NoInternetConnectionException {
        Path path = new Path(super.getPathName());
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < super.getOrderedObjects().size(); i++) {
            int objectId = super.getOrderedObjects().get(i);
            objects.add(new Object(objectId));
        }
        path.setObjects(objects);
        return super.pathRepository.editPath(path);
    }
}
