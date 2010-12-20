/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author viv
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
