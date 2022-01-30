package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.Place;

import java.util.List;

@Dao
public interface LocalPlaceDAO {
    @Insert
    void insertPlace(Place place);

    @Update
    void updatePlace(Place place);

    @Delete
    void deletePlace(Place place);

    @Query("SELECT * FROM place")
    List<Place> getAllPlaces();

    @Query("SELECT * FROM place WHERE id = :placeId")
    Place getPlaceById(int placeId);
}
