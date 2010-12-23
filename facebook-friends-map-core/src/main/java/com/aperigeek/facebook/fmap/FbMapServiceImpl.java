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

import com.aperigeek.facebook.fmap.fbapi.CachedFbClient;
import com.aperigeek.facebook.fmap.fbapi.FbClient;
import com.aperigeek.facebook.fmap.fbapi.FbClientImpl;
import com.aperigeek.facebook.fmap.fbapi.FbException;
import com.aperigeek.facebook.fmap.fbapi.FbFriend;
import com.aperigeek.facebook.fmap.fbapi.FbLocation;
import com.aperigeek.facebook.fmap.geo.CachedGeocodingClient;
import com.aperigeek.facebook.fmap.geo.GeoException;
import com.aperigeek.facebook.fmap.geo.GeoLocation;
import com.aperigeek.facebook.fmap.geo.GeocodingClient;
import com.aperigeek.facebook.fmap.geo.osm.NominatimGeocodingClient;
import com.aperigeek.facebook.fmap.image.BoundingBox;
import com.aperigeek.facebook.fmap.image.ImagePlotter;
import com.aperigeek.facebook.fmap.image.proj.LinearProjection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Vivien Barousse
 */
public class FbMapServiceImpl implements FbMapService {

    public BufferedImage generateMap(String token) throws FbException, GeoException {
        File home = new File(System.getProperty("user.home"));
        File cacheRoot = new File(home, ".fmap/cache");
        File geoCache = new File(cacheRoot, "maps");
        File fbCache = new File(cacheRoot, "fb");

        FbClient rawFb = new FbClientImpl(token);
        FbClient fb = new CachedFbClient(fbCache, rawFb);
        GeocodingClient rawMaps = new NominatimGeocodingClient();
        GeocodingClient maps = new CachedGeocodingClient(geoCache, rawMaps);
        ImagePlotter plotter = new ImagePlotter(2368, 1179, new LinearProjection(2368, 1179));

        List<FbFriend> friends = fb.getFriends();
        List<FbLocation> fbLocations = map(friends, new FbLocationMapper(fb));
        List<GeoLocation> locations = map(fbLocations, new GMapsLocationMapper(maps));

        FbLocation fbLocation = fb.getCurrentLocation();
        GeoLocation location = maps.geocode(fbLocation.getName());

        plotter.setBoundingBox(new BoundingBox(89.89, -89.28, 180.49, -178.29));

        BufferedImage background = null;
        try {
            background = ImageIO.read(this.getClass().getResourceAsStream("/maps/fb_orig_map.jpg"));
        } catch (Exception ex) {
            // Unable to load background, using none
        }

        BufferedImage image = plotter.plot(location, locations, background);

        return image;
    }

    protected <O, T> List<T> map(List<O> orig, Mapper<O, T> mapper) {
        List<T> ts = new ArrayList<T>();
        for (O o : orig) {
            T t = mapper.map(o);
            if (t != null) {
                ts.add(t);
            }
        }
        return ts;
    }

    protected static interface Mapper<O, T> {

        public T map(O o);

    }

    protected static class FbLocationMapper implements Mapper<FbFriend, FbLocation> {

        private FbClient client;

        public FbLocationMapper(FbClient client) {
            this.client = client;
        }

        public FbLocation map(FbFriend o) {
            try {
                return client.getFriendLocation(o);
            } catch (FbException ex) {
                return null;
            }
        }

    }

    protected static class GMapsLocationMapper implements Mapper<FbLocation, GeoLocation> {

        private GeocodingClient client;

        public GMapsLocationMapper(GeocodingClient client) {
            this.client = client;
        }

        public GeoLocation map(FbLocation o) {
            try {
                return client.geocode(o.getName());
            } catch (GeoException ex) {
                throw new RuntimeException("Unable to get coordinates for " + o.getName(), ex);
            }
        }

    }

}
