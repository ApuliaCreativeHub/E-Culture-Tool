package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Object {
    @PrimaryKey
    private int id;

    @ColumnInfo(name="uriImg")
    private String uriImg;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "zone_id")
    private int zoneId;

    private String normalSizeImg;

    public Object() {
    }

    @Ignore
    public Object(int id) {
        this.id = id;
    }

    @Ignore
    public Object(int id, String name, String description, String uriImg, int zoneId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uriImg = uriImg;
        this.zoneId = zoneId;
    }

    @Ignore
    public Object(String name, String description, String uriImg, int zoneId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uriImg = uriImg;
        this.zoneId = zoneId;
    }

    @Ignore
    public Object(int id, String name, String description, int zoneId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.zoneId = zoneId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNormalSizeImg() {
        return normalSizeImg;
    }

    public void setNormalSizeImg(String normalSizeImg) {
        this.normalSizeImg = normalSizeImg;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }
}
