package com.apuliacreativehub.eculturetool.ui.places;

import com.apuliacreativehub.eculturetool.data.entity.Object;

import java.util.ArrayList;

public class NodeObject extends Object {

    private Double weight;

    public NodeObject(int id, String name, String description, String uriImg, int zoneId) {
        super(id, name, description, uriImg, zoneId);
        this.weight = null;
    }

    public NodeObject(Object object) {
        super(object.getId(), object.getName(), object.getDescription(), object.getUriImg(), object.getZoneId());
        this.setNormalSizeImg(object.getNormalSizeImg());
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

    public static ArrayList<NodeObject> getNodeObjectAll(ArrayList<Object> objects){
        ArrayList<NodeObject> nodeObjects = new ArrayList<>();
        for(Object object : objects) {
            nodeObjects.add(new NodeObject(object));
        }
        return nodeObjects;
    }

}
