package com.apuliacreativehub.eculturetool.data.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Place {
    @PrimaryKey
    public int id;

    @ColumnInfo(name="uriImg")
    public String uriImg;

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

    public Place(String uriImg, String name, String address, String description) {
        this.uriImg = uriImg;
        this.name = name;
        this.address = address;
        this.description = description;
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

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }

}