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

    @Query("SELECT pl.* FROM place pl " +
            "JOIN zone z ON z.place_id = pl.id " +
            "JOIN object o ON o.zone_id = z.id " +
            "JOIN ispresentin ipi ON ipi.object_id = o.object_id " +
            "JOIN path p ON p.path_id = ipi.path_id " +
            "WHERE p.path_id= :pathId " +
            "GROUP BY p.path_id")
    Place getPlaceByPathId(int pathId);
}
