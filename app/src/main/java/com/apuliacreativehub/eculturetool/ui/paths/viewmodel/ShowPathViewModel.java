package com.apuliacreativehub.eculturetool.ui.paths.viewmodel;

import androidx.lifecycle.ViewModel;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;

import java.util.ArrayList;
import java.util.List;

public class ShowPathViewModel extends ViewModel {
    private List<Object> mDataset;
    private int numberColumn;
    private int orientation;
    private Path path;

    public void fillDatasetFromPath() {
        if (path != null) {
            mDataset = path.getObjects();
        }
    }

    public List<Object> getmDataset() {
        return mDataset;
    }

    public void setmDataset(ArrayList<Object> mDataset) {
        this.mDataset = mDataset;
    }

    public int getNumberColumn() {
        return numberColumn;
    }

    public void setNumberColumn(int numberColumn) {
        this.numberColumn = numberColumn;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
