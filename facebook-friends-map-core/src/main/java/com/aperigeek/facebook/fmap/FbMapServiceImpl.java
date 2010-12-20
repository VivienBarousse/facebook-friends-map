/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import com.aperigeek.facebook.fmap.image.ImagePlotter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author viv
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
        ImagePlotter plotter = new ImagePlotter();

        List<FbFriend> friends = fb.getFriends();
        List<FbLocation> fbLocations = map(friends, new FbLocationMapper(fb));
        List<GeoLocation> locations = map(fbLocations, new GMapsLocationMapper(maps));

        FbLocation fbLocation = fb.getCurrentLocation();
        GeoLocation location = maps.geocode(fbLocation.getName());

        BufferedImage image = plotter.plot(2048, 2048, location, locations, null);

        return image;
    }

    protected <O,T> List<T> map(List<O> orig, Mapper<O,T> mapper) {
        List<T> ts = new ArrayList<T>();
        for (O o : orig) {
            T t = mapper.map(o);
            if (t != null) {
                ts.add(t);
            }
        }
        return ts;
    }

    protected static interface Mapper<O,T> {

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
