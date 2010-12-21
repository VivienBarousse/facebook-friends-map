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

/**
 *
 * @author Vivien Barousse
 */
public class BoundingBox {

    private double north;

    private double south;

    private double east;

    private double west;

    public BoundingBox(double north, double south, double east, double west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoundingBox other = (BoundingBox) obj;
        if (Double.doubleToLongBits(this.north) != Double.doubleToLongBits(other.north)) {
            return false;
        }
        if (Double.doubleToLongBits(this.south) != Double.doubleToLongBits(other.south)) {
            return false;
        }
        if (Double.doubleToLongBits(this.east) != Double.doubleToLongBits(other.east)) {
            return false;
        }
        if (Double.doubleToLongBits(this.west) != Double.doubleToLongBits(other.west)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.north) ^ (Double.doubleToLongBits(this.north) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.south) ^ (Double.doubleToLongBits(this.south) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.east) ^ (Double.doubleToLongBits(this.east) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.west) ^ (Double.doubleToLongBits(this.west) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "BoudingBox{" + "north=" + north + "south=" + south + "east=" + east + "west=" + west + '}';
    }

    public double getEast() {
        return east;
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    public double getWest() {
        return west;
    }

}
