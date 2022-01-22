package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Place {
    @PrimaryKey
    public int id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="address")
    public String address;

    @ColumnInfo(name="description")
    public String description;

    @ColumnInfo(name="lat")
    public String lat;

    @ColumnInfo(name="longit")
    public String longit;
}
