package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.PathWithObjects;

import java.util.List;

@Dao
public interface LocalPathDAO {
    @Insert
    void insertPath(PathWithObjects path);

    @Transaction
    @Query("SELECT * FROM path WHERE user_id=:userId")
    List<PathWithObjects> getAllPathsByUserId(int userId);

    @Transaction
    @Query("SELECT * FROM path " +
            "JOIN isPresentIn ON path.id=isPresentIn.path_id " +
            "JOIN object ON isPresentIn.object_id=object.id " +
            "JOIN zone ON object.zone_id=zone.id " +
            "JOIN place ON zone.place_id=place.id " +
            "WHERE place.id=:placeId")
    List<PathWithObjects> getAllPathsByPlaceId(int placeId);

    @Update
    void updatePath(PathWithObjects path);

    @Delete
    void deletePath(Path path);

    @Query("SELECT * FROM path WHERE id=:pathId")
    Path getPathById(int pathId);
}