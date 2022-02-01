package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.Path;

import java.util.List;

@Dao
public interface LocalPathDAO {
    @Insert
    void insertPath(Path path);

    @Query("SELECT * FROM path WHERE user_id=:userId")
    List<Path> getAllPathsByUserId(int userId);

    @Query("SELECT path.* FROM path " +
            "JOIN isPresentIn ON path.path_id=isPresentIn.path_id " +
            "JOIN object ON isPresentIn.object_id=object.object_id " +
            "JOIN zone ON object.zone_id=zone.id " +
            "JOIN place ON zone.place_id=place.id " +
            "WHERE place.id=:placeId " +
            "GROUP BY path.path_id")
    List<Path> getAllPathsByPlaceId(int placeId);

    @Query("SELECT path.* FROM path " +
            "JOIN isPresentIn ON path.path_id=isPresentIn.path_id " +
            "JOIN object ON isPresentIn.object_id=object.object_id " +
            "JOIN zone ON object.zone_id=zone.id " +
            "JOIN place ON zone.place_id=place.id " +
            "WHERE path.user_id=place.user_id AND place.id=:placeId " +
            "GROUP BY path.path_id")
    List<Path> getAllCuratorPathsByPlaceId(int placeId);

    @Update
    void updatePath(Path path);

    @Delete
    void deletePath(Path path);

    @Query("SELECT * FROM path WHERE path_id=:pathId")
    Path getPathById(int pathId);
}
