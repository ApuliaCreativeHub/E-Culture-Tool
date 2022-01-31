package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PathWithObjects {
    @Embedded
    public Path path;

    @Relation(
            parentColumn = "path_id",
            entityColumn = "object_id",
            associateBy = @Junction(IsPresentIn.class)
    )
    public List<Object> objects;
}
