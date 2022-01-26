package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.apuliacreativehub.eculturetool.data.repository.PlaceRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

abstract public class AbstractObjectViewModel extends AndroidViewModel {
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;
    private static final int MIN_LENGTH_DESCRIPTION = 10;
    private static final int MAX_LENGTH_DESCRIPTION = 100;

    protected String name = "";
    protected String room = "";
    protected String description = "";
    protected Uri image = null;

    protected AbstractObjectViewModel(Application application) {
        super(application);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameCorrect(String name) {
        return name.length() >= MIN_LENGTH_NAME && name.length() <= MAX_LENGTH_NAME;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean isRoomSelected(String room) {
        return !room.equals("");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDescriptionCorrect(String description) {
        return description.length() >= MIN_LENGTH_DESCRIPTION && description.length() <= MAX_LENGTH_DESCRIPTION;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public boolean isImageUploaded(Uri image) {
        return image != null;
    }

}