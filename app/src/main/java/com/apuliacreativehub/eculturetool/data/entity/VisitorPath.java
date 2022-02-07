package com.apuliacreativehub.eculturetool.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class VisitorPath {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "path_id")
    private int visitorPathId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "user_id")
    private int userId;

    @Ignore
    private List<Object> objects;

    @Ignore
    private Place place;

    public VisitorPath(int visitorPathId, String name) {
        this.visitorPathId = visitorPathId;
        this.name = name;
    }

    /**
     * Used to build a VisitorPath object from a Path one.
     * Field visitorPathId will correspond to id.
     *
     * @param path Path to convert
     **/
    @Ignore
    public VisitorPath(Path path) {
        this.visitorPathId = path.getId();
        this.name = path.getName();
        this.userId = path.getUserId();
        this.objects = path.getObjects();
        this.place = path.getPlace();
    }

    public int getVisitorPathId() {
        return visitorPathId;
    }

    public void setVisitorPathId(int visitorPathId) {
        this.visitorPathId = visitorPathId;
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
