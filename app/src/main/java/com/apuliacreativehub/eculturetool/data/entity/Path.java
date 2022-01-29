package com.apuliacreativehub.eculturetool.data.entity;

public class Path {

    private int placeId;
    private String pathName;
    private String placeName;
    private String placeAddress;
    private String placeImage;

    public Path(int placeId, String pathName, String placeName, String placeAddress, String placeImage) {
        this.placeId = placeId;
        this.pathName = pathName;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeImage = placeImage;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
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