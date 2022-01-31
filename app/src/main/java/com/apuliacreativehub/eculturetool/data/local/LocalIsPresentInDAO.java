package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Insert;

import com.apuliacreativehub.eculturetool.data.entity.IsPresentIn;

@Dao
public interface LocalIsPresentInDAO {
    @Insert
    void insertRelation(IsPresentIn relation);
}
