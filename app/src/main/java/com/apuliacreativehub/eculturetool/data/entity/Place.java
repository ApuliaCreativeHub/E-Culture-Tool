package com.apuliacreativehub.eculturetool.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Place implements Parcelable {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "uri_img")
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

    @SerializedName("userId")
    @ColumnInfo(name = "user_id")
    private int userId;

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
    public Place(int id, String name, String address, String description, String uriImg) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    protected Place(Parcel parcel){
        id = parcel.readInt();
        name = parcel.readString();
        address = parcel.readString();
        description = parcel.readString();
        lat = parcel.readString();
        lon = parcel.readString();
        uriImg = parcel.readString();
        normalSizeImg = parcel.readString();
        thumbnail = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(description);
        parcel.writeString(lat);
        parcel.writeString(lon);
        parcel.writeString(uriImg);
        parcel.writeString(normalSizeImg);
        parcel.writeString(thumbnail);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }
        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}