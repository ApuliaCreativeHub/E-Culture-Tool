package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"object_id", "path_id", "order"})
public class VisitorIsPresentIn {
    @ColumnInfo(name = "object_id")
    public int objectId;
    @ColumnInfo(name = "path_id")
    public int visitorPathId;
    @ColumnInfo(name = "order")
    public int order;

    public VisitorIsPresentIn(int objectId, int visitorPathId, int order) {
        this.objectId = objectId;
        this.visitorPathId = visitorPathId;
        this.order = order;
    }
}
