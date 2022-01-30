package com.apuliacreativehub.eculturetool.ui.component;

import com.mapbox.geojson.Point;

import java.util.Comparator;

public class MapboxHelper implements Comparator<Point> {

    public static final float DEFAULT_TOLERANCE = 0.05f;
    private final Point origin;
    private final float tolerance;

    public MapboxHelper(Point origin, float tolerance) {
        this.origin = origin;
        this.tolerance = tolerance;
    }

    private Double getMinDistance(Point point) {
        return Math.min(Math.abs(this.origin.latitude() - point.latitude()),
                Math.abs(this.origin.longitude() - point.longitude()));
    }

    @Override
    public int compare(Point first, Point second) {
        Double resultPointFirst = getMinDistance(first);
        Double resultPointSecond = getMinDistance(second);

        if(resultPointFirst == resultPointSecond) return 0;
        else if(resultPointFirst > resultPointSecond) return 1;
        return -1;
    }

    public boolean isPointValid(Point p) {
        return ((p.longitude() - origin.longitude()) <= tolerance) &&
                ((p.latitude() - origin.latitude()) <= tolerance);
    }
}
