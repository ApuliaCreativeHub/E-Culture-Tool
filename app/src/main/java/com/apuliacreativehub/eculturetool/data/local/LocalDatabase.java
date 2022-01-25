package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.apuliacreativehub.eculturetool.data.entity.Place;

@Database(entities = {Place.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract LocalPlaceDAO placeDAO();

    // Add other DAOs abstract methods here
}
