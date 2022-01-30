package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Zone {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "place_id")
    private int placeId;

    public Zone() {
    }

    @Ignore
    public Zone(int id){
        this.id = id;
    }
    @Ignore
    public Zone(String name, int placeId) {
        this.name = name;
        this.placeId = placeId;
    }

    @Ignore
    public Zone(int id, String name, int placeId) {
        this.id = id;
        this.name = name;
        this.placeId = placeId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
