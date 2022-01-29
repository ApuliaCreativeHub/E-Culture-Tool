package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.Object;

import java.util.List;

@Dao
public interface LocalObjectDAO {
    @Query("SELECT * FROM object WHERE zone_id = :zoneId")
    List<Object> getAllObjectsByZoneId(int zoneId);

    @Query("SELECT * FROM object WHERE id = :objectId")
    Object getObjectById(int objectId);

    @Insert
    void insertObject(Object object);

    @Update
    void updateObject(Object object);

    @Delete
    void deleteObject(Object object);
}
