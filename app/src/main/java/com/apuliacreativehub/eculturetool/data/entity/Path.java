package com.apuliacreativehub.eculturetool.data.entity;

import java.util.Date;

public class Path {

    private String id;
    private String name;
    private String shortDescription;
    private String placeName;
    private String imageURL;
    private Date lastUpdate;
    //TODO: check all field

    public Path(String id, String name, String placeName, String shortDescription, String imageUrl, Date lastUpdate) {
        this.id = id;
        this.name = name;
        this.placeName = placeName;
        this.shortDescription = shortDescription;
        this.imageURL = imageUrl;
        this.lastUpdate = lastUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
