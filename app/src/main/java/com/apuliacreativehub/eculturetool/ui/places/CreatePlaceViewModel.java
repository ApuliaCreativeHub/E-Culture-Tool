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

public class CreatePlaceViewModel extends AbstractPlaceViewModel {

    public CreatePlaceViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> addPlace(){
        Place place = new Place(name, address, description, getRealPathFromURI(getApplication().getApplicationContext(), image));
        Log.i("pathUri", place.getUriImg());
        return repository.addPlace(getApplication().getApplicationContext(), place);
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("pathFromUri", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}