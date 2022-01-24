package com.apuliacreativehub.eculturetool.ui.places;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

import com.apuliacreativehub.eculturetool.data.repository.PlaceRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

abstract public class AbstractPlaceViewModel extends AndroidViewModel {
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 25;
    private static final int MIN_LENGTH_ADDRESS = 5;
    private static final int MAX_LENGTH_ADDRESS = 50;
    private static final int MIN_LENGTH_DESCRIPTION = 10;
    private static final int MAX_LENGTH_DESCRIPTION = 100;
    protected final PlaceRepository repository;

    protected String name = "";
    protected String address = "";
    protected String description = "";
    protected Uri image = null;


    protected AbstractPlaceViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        repository = new PlaceRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAddressCorrect(String address) {
        return address.length() >= MIN_LENGTH_ADDRESS && address.length() <= MAX_LENGTH_ADDRESS;
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