/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.image.proj;

import com.aperigeek.facebook.fmap.geo.GeoLocation;
import com.aperigeek.facebook.fmap.image.BoundingBox;
import com.aperigeek.facebook.fmap.image.Projection;

/**
 *
 * @author viv
 */
public class MercatorProjection implements Projection {

    private int width;

    private int height;

    private BoundingBox boundingBox;

    public MercatorProjection(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int toX(GeoLocation l) {
        double x = (l.getLongitude() + 180) - (boundingBox.getWest() + 180);
        return (int) (x * width / (boundingBox.getEast() - boundingBox.getWest()));
    }

    public int toY(GeoLocation l) {
        double y = mercator(l.getLatitude());
        double n = mercator(boundingBox.getNorth());
        double s = mercator(boundingBox.getSouth());
        return (int) (((y - n) / (s - n) * 2) / 2 * height);
    }

    protected double mercator(double lat) {
        return (1 - (Math.log(Math.tan(Math.toRadians(lat)) + 1.0 / Math.cos(Math.toRadians(lat))) / Math.PI));
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
