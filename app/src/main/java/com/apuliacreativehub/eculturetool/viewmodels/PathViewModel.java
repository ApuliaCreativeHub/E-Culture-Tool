package com.apuliacreativehub.eculturetool.viewmodels;

import androidx.lifecycle.ViewModel;

import java.util.Date;

public class PathViewModel extends ViewModel {

    private String place;
    private String name;
    private Date lastUsed;

    public PathViewModel(String place, String name, Date lastUsed) {
        this.place = place;
        this.name = name;
        this.lastUsed = lastUsed;
    }

    public void setLocation(String place) {
        this.place = place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getPlace() {
        return this.place;
    }

    public String getName() {
        return this.name;
    }

    public Date getLastUsed() {
        return this.lastUsed;
    }

}
