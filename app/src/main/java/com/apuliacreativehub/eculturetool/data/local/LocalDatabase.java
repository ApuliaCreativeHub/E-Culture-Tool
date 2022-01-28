package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;

@Database(entities = {Place.class, Zone.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract LocalPlaceDAO placeDAO();

    public abstract LocalZoneDAO zoneDAO();

    public abstract LocalObjectDAO objectDAO();

    // Add other DAOs abstract methods here
}
