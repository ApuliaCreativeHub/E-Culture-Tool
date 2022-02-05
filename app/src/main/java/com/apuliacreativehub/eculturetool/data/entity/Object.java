package com.apuliacreativehub.eculturetool.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Object implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "object_id")
    private int id;

    @ColumnInfo(name = "uri_img")
    private String uriImg;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "zone_id")
    private int zoneId;

    private String normalSizeImg;

    @Ignore
    private Zone zone;

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

    public static final Creator<Object> CREATOR = new Creator<Object>() {
        @Override
        public Object createFromParcel(Parcel in) {
            return new Object(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    protected Object(Parcel in) {
        id = in.readInt();
        uriImg = in.readString();
        name = in.readString();
        description = in.readString();
        zoneId = in.readInt();
        normalSizeImg = in.readString();
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

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(uriImg);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(zoneId);
        parcel.writeString(normalSizeImg);
    }
}
