package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"object_id", "path_id", "order"})
public class IsPresentIn {
    @ColumnInfo(name="object_id")
    int objectId;
    @ColumnInfo(name="path_id")
    int pathId;
    @ColumnInfo(name="order")
    int order;
}
