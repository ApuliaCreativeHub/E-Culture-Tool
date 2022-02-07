package com.apuliacreativehub.eculturetool.ui.places.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.PlaceRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.di.ECultureTool;
import com.mapbox.geojson.Point;

import java.util.ArrayList;

public class MapFragmentViewModel extends AndroidViewModel {
    private Point[] points;
    private ArrayList<Place> places;
    private final PlaceRepository repository;

    public MapFragmentViewModel(@NonNull Application application) {
        super(application);
        ECultureTool app = getApplication();
        repository = new PlaceRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    public MutableLiveData<RepositoryNotification<ArrayList<Place>>> getAllPlaces(){
        return repository.getAllPlaces();
    }

    public void setPointsAndPlaces(ArrayList<Place> places){
        this.places = places;
        this.points = getPointsFromPlaces(places);
    }

    public Point[] getPoints(){
        return this.points;
    }

    public Place getPlaceFromPoint(Point point){
        String strLon = String.valueOf(point.longitude());
        String strLat = String.valueOf(point.latitude());

        for(int i = 0; i < places.size(); i++){
            if(places.get(i).getLat().equals(strLat) && places.get(i).getLon().equals(strLon)){
                return places.get(i);
            }
        }
        return null;
    }

    private Point[] getPointsFromPlaces(ArrayList<Place> places){
        ArrayList<Point> points = new ArrayList<>();
        for(int i = 0; i < places.size(); i++){
            points.add(Point.fromLngLat(Double.parseDouble(places.get(i).getLon()), Double.parseDouble(places.get(i).getLat())));
        }
        return points.toArray(new Point[0]);
    }
}
