package com.apuliacreativehub.eculturetool.ui.places;

import com.apuliacreativehub.eculturetool.data.entity.Artifact;

public class NodeArtifact extends Artifact {

    private Double weight;

    public NodeArtifact(int id, String name, String description, String thumbnail) {
        super(id, name, description, thumbnail);
        weight = null;
    }

    public NodeArtifact(int id, String name, String description, String thumbnail, Double weight) {
        super(id, name, description, thumbnail);
        this.weight = weight;
    }

    public NodeArtifact(Artifact artifact, Double weight) {
        super(artifact.getId(), artifact.getName(), artifact.getDescription(), artifact.getThumbnail());
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String toString() {
        return getWeight() + " - " + getName();
    }
}
