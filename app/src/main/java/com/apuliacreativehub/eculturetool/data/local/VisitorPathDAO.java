package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.VisitorPath;

import java.util.List;

@Dao
public interface VisitorPathDAO {
    @Insert
    long insertPath(VisitorPath path);

    @Query("SELECT * FROM VisitorPath")
    List<VisitorPath> getAllYourPaths();

    @Query("SELECT vp.* FROM VisitorPath vp " +
            "JOIN VisitorisPresentIn vipi ON vp.path_id=vipi.path_id " +
            "JOIN object ON vipi.object_id=object.object_id " +
            "JOIN zone ON object.zone_id=zone.id " +
            "JOIN place ON zone.place_id=place.id " +
            "WHERE place.id=:placeId " +
            "GROUP BY vp.path_id")
    List<VisitorPath> getAllPathsByPlaceId(int placeId);

    @Query("SELECT * FROM VisitorPath WHERE path_id=:pathId")
    VisitorPath getPathById(int pathId);

    @Update
    void updatePath(VisitorPath path);

    @Delete
    void deletePath(VisitorPath path);
}
