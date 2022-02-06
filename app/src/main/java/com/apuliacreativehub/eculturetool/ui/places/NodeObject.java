package com.apuliacreativehub.eculturetool.ui.places;

import com.apuliacreativehub.eculturetool.data.entity.Object;

import java.util.ArrayList;
import java.util.List;

public class NodeObject extends Object {
    private Double weight;
    private boolean checked;

    public NodeObject(int id, String name, String description, String uriImg, int zoneId) {
        super(id, name, description, uriImg, zoneId);
        this.weight = null;
    }

    public NodeObject(Object object) {
        super(object.getId(), object.getName(), object.getDescription(), object.getUriImg(), object.getZoneId());
        this.setNormalSizeImg(object.getNormalSizeImg());
        this.setZone(object.getZone());
        this.weight = null;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String toString() {
        return getName() + " - " + getWeight();
    }

    public static NodeObject getNodeObject(Object object){
        return new NodeObject(object);
    }

    public static List<NodeObject> getNodeObjectAll(List<Object> objects) {
        ArrayList<NodeObject> nodeObjects = new ArrayList<>();
        for (Object object : objects) {
            nodeObjects.add(new NodeObject(object));
        }
        return nodeObjects;
    }

    public void check() {
        this.checked = true;
    }

    public void uncheck() {
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }
}
