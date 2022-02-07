package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.apuliacreativehub.eculturetool.data.entity.IsPresentIn;

@Dao
public interface LocalIsPresentInDAO {
    @Insert
    void insertRelation(IsPresentIn relation);

    @Query("DELETE FROM IsPresentIn " +
            "WHERE IsPresentIn.path_id=:pathId")
    void deleteRelationsByPathId(int pathId);

    @Query("DELETE FROM ispresentin")
    void clearTable();
}
