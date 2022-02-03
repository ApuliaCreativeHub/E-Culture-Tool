package com.apuliacreativehub.eculturetool.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Path implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "path_id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "user_id")
    private int userId;

    @Ignore
    private List<Object> objects;

    @Ignore
    private Place place;

    public Path(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Path(int id) {
        this.id = id;
    }

    @Ignore
    public Path(String name) {
        this.name = name;
    }

    public static final Creator<Path> CREATOR = new Creator<Path>() {
        @Override
        public Path createFromParcel(Parcel in) {
            return new Path(in);
        }

        @Override
        public Path[] newArray(int size) {
            return new Path[size];
        }
    };

    protected Path(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        userId = parcel.readInt();
        place = parcel.readParcelable(Place.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(userId);
        parcel.writeArray(objects.toArray());
        parcel.writeParcelable(place, 0);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}