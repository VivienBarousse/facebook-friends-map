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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Vivien Barousse
 */
public class CenteredImagePlotter extends ImagePlotter {

    public CenteredImagePlotter(Projection projection) {
        super(projection);
    }

    public CenteredImagePlotter(int width, int height) {
        super(width, height);
    }
    
    public BufferedImage plot(GeoLocation center, Collection<GeoLocation> points,
            Image background) {
        
        List<Line> lines = new ArrayList<Line>();
        
        for (GeoLocation p : points) {
            lines.add(new Line(center, p));
        }
        
        return plot(lines, background);
        
    }
    
}
