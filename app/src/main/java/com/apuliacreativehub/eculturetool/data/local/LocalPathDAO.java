package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.Path;

import java.util.List;

public interface LocalPathDAO {
    @Insert
    void insertPath(Path path);

    @Query("SELECT * FROM path WHERE user_id=:userId")
    List<Path> getAllPathsByUserId(int userId);

    @Query("SELECT * FROM path " +
            "JOIN isPresentIn ON path.id=isPresentIn.pathId " +
            "JOIN object ON isPresentIn.objectId=object.id " +
            "JOIN zone ON object.zone_id=zone.id " +
            "JOIN place ON zone.place_id=place.id " +
            "WHERE place.id=:placeId")
    List<Path> getAllPathsByPlaceId(int placeId);

    @Update
    void updatePath(Path path);

    @Delete
    void deletePath(Path path);
}
