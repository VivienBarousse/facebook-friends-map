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

package com.aperigeek.facebook.fmap;

import com.aperigeek.facebook.fmap.fbapi.FbClient;
import com.aperigeek.facebook.fmap.fbapi.FbClientImpl;
import com.aperigeek.facebook.fmap.fbapi.FbException;
import com.aperigeek.facebook.fmap.geo.GeoException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Vivien Barousse
 */
public class CachedFbMapService implements FbMapService {

    private FbMapService wrapped;

    public CachedFbMapService(FbMapService wrapped) {
        this.wrapped = wrapped;
    }

    public BufferedImage generateMap(String token) throws FbException, GeoException {
        File home = new File(System.getProperty("user.home"));
        File cacheRoot = new File(home, ".fmap/cache");
        File fbmapsCache = new File(cacheRoot, "fbmaps");

        if (!fbmapsCache.exists()) {
            fbmapsCache.mkdirs();
        }

        FbClient rawFb = new FbClientImpl(token);
        long userId = rawFb.getUserId();

        File cachedMap = new File(fbmapsCache, userId + ".png");
        if (cachedMap.exists()) {
            try {
                BufferedImage image = ImageIO.read(cachedMap);
                return image;
            } catch (IOException ex) {
                // Unable to read map, generating again...
            }
        }

        BufferedImage image = wrapped.generateMap(token);
        try {
            ImageIO.write(image, "png", cachedMap);
        } catch (IOException ex) {
            // Unable to write to cache...
            // TODO: Better exception handling on this one...
        }
        return image;
    }

}
