package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Place {
    @PrimaryKey
    private int id;

    @ColumnInfo(name="uriImg")
    private String uriImg;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "lat")
    private String lat;

    @SerializedName("long")
    @ColumnInfo(name = "long")
    private String lon;

    private String normalSizeImg;
    private String thumbnail;

    public Place() {

    }

    @Ignore
    public Place(String name, String address, String description, String uriImg) {
        this.uriImg = uriImg;
        this.name = name;
        this.address = address;
        this.description = description;
    }

    @Ignore
    public Place(String name, String address, String description, String normalSizeImg, String thumbnail) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.normalSizeImg = normalSizeImg;
        this.thumbnail = thumbnail;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }

    public String getNormalSizeImg() {
        return normalSizeImg;
    }

    public void setNormalSizeImg(String normalSizeImg) {
        this.normalSizeImg = normalSizeImg;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}