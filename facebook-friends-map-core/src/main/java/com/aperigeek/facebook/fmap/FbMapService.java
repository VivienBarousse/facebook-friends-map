/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap;

import com.aperigeek.facebook.fmap.fbapi.FbException;
import com.aperigeek.facebook.fmap.geo.GeoException;
import java.awt.image.BufferedImage;

/**
 *
 * @author viv
 */
public interface FbMapService {
    
    public BufferedImage generateMap(String token) throws FbException, GeoException;
    
}
