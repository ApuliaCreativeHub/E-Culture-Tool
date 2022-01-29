package com.apuliacreativehub.eculturetool.ui.places;

import com.apuliacreativehub.eculturetool.data.entity.Object;

public class NodeArtifact extends Object {

    private Double weight;

    public NodeArtifact(int id, String name, String description, String uriImg, int zoneId) {
        super(id, name, description, uriImg, zoneId);
        this.weight = null;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
