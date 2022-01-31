package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"object_id", "path_id", "order"})
public class IsPresentIn {
    @ColumnInfo(name = "object_id")
    public int objectId;
    @ColumnInfo(name = "path_id")
    public int pathId;
    @ColumnInfo(name = "order")
    public int order;
}
