/*
 * Copyright (C) 2010 Vivien Barousse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.aperigeek.facebook.fmap.image;

import com.aperigeek.facebook.fmap.geo.GeoLocation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 *
 * @author Vivien Barousse
 */
public class ImagePlotter {

    private BoundingBox boundingBox = new BoundingBox(85, -85, 180, -180);

    public BufferedImage plot(int width, int height, GeoLocation center, Collection<GeoLocation> points,
            Image background) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.drawImage(background, 0, 0, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (GeoLocation p : points) {
            g.setColor(new Color(75, 75, 255));
            drawGreatCircle(g, center, p, width, height);
        }

        return image;
    }

    protected void drawGreatCircle(Graphics g, GeoLocation l1, GeoLocation l2, int width, int height) {
        double distance = distance(l1, l2);
        int steps = (int) (distance / 150); // One point every 150 kms
        double bearing = bearing(l1, l2);
        double stepDist = distance / steps;
        GeoLocation lastPoint = l1;
        for (int i = 1; i < steps; i++) {
            GeoLocation next = pointFromBearingAndDistance(l1, bearing, stepDist * i);
            int x1 = toX(lastPoint, width),
                    x2 = toX(next, width),
                    y1 = toY(lastPoint, height),
                    y2 = toY(next, height);
            if (Math.abs(x1 - x2) > width / 2) {
                if (x1 < x2) {
                    g.drawLine(x1, y1, x2 - width, y2);
                    g.drawLine(width + x1, y1, x2, y2);
                } else {
                    g.drawLine(x1, y1, x2 + width, y2);
                    g.drawLine(x1 - width, y1, x2, y2);
                }
            } else {
                g.drawLine(toX(lastPoint, width), toY(lastPoint, height),
                        toX(next, width), toY(next, height));
            }
            lastPoint = next;
        }
        g.drawLine(toX(lastPoint, width), toY(lastPoint, height),
                toX(l2, width), toY(l2, height));
    }

    protected GeoLocation pointFromBearingAndDistance(GeoLocation l1, double bearing, double d) {
        double r = 6371; // km

        double dist = d / r;
        double lat1 = Math.toRadians(l1.getLatitude());
        double lon1 = Math.toRadians(l1.getLongitude());
        double brng = Math.toRadians(bearing);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist)
                + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1),
                Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));

        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        return new GeoLocation(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    protected double distance(GeoLocation l1, GeoLocation l2) {
        double r = 6371; // km

        double lat1 = Math.toRadians(l1.getLatitude());
        double lat2 = Math.toRadians(l2.getLatitude());
        double lon1 = Math.toRadians(l1.getLongitude());
        double lon2 = Math.toRadians(l2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;

        return d;
    }

    protected double bearing(GeoLocation l1, GeoLocation l2) {
        double dLon = Math.toRadians(l2.getLongitude() - l1.getLongitude());
        double lat1 = Math.toRadians(l1.getLatitude());
        double lat2 = Math.toRadians(l2.getLatitude());

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2)
                - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.toDegrees(Math.atan2(y, x));
        return (brng + 360) % 360;
    }

    protected int toX(GeoLocation l, int width) {
        double x = (l.getLongitude() + 180);
        return (int) (x * width / (boundingBox.getEast() - boundingBox.getWest()));
    }

    protected int toY(GeoLocation l, int height) {
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

}
