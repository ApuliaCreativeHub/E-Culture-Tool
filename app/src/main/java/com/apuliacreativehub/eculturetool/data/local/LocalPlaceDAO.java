package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Insert;

import com.apuliacreativehub.eculturetool.data.entity.Place;

@Dao
public interface LocalPlaceDAO {
    @Insert
    void add(Place place);
}
