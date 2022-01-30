package com.apuliacreativehub.eculturetool.data.entity;

public class Path {

    private int pathId;
    private String pathName;
    private String placeName;
    private String placeAddress;
    private String placeImage;

    public Path(int pathId, String pathName, String placeName, String placeAddress, String placeImage) {
        this.pathId = pathId;
        this.pathName = pathName;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeImage = placeImage;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

}