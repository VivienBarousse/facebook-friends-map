/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.image;

import com.aperigeek.facebook.fmap.geo.GeoLocation;

/**
 *
 * @author viv
 */
public interface Projection {

    public int toX(GeoLocation l);

    public int toY(GeoLocation l);

    public int getHeight();

    public void setHeight(int height);

    public int getWidth();

    public void setWidth(int width);

    public BoundingBox getBoundingBox();

    public void setBoundingBox(BoundingBox box);

}
