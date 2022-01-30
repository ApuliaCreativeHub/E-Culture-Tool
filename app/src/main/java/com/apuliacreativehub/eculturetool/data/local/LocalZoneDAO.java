package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apuliacreativehub.eculturetool.data.entity.Zone;

import java.util.List;

@Dao
public interface LocalZoneDAO {
    @Query("SELECT * FROM zone WHERE place_id = :placeId")
    List<Zone> getAllZonesByPlaceId(int placeId);

    @Query("SELECT * FROM zone WHERE id = :zoneId")
    Zone getZoneById(int zoneId);

    @Insert
    void insertZone(Zone zone);

    @Update
    void updateZone(Zone zone);

    @Delete
    void deleteZone(Zone zone);
}
