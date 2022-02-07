package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.apuliacreativehub.eculturetool.data.entity.VisitorIsPresentIn;

@Dao
public interface VisitorIsPresentInDAO {
    @Insert
    void insertRelation(VisitorIsPresentIn relation);

    @Query("DELETE FROM VisitorIsPresentIn " +
            "WHERE VisitorIsPresentIn.path_id=:visitorPathId")
    void deleteRelationsByPathId(int visitorPathId);
}
