/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.geo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author viv
 */
public class CachedGeocodingClient implements GeocodingClient {

    private File rootFolder;

    private GeocodingClient wrapped;

    public CachedGeocodingClient(File rootFolder, GeocodingClient wrapped) {
        this.rootFolder = rootFolder;
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        this.wrapped = wrapped;
    }

    public GeoLocation geocode(String address) throws GeoException {
        GeoLocation location = getFromCache(address);
        if (location == null) {
            location = wrapped.geocode(address);
            if (location != null) {
                setInCache(address, location);
            }
        }

        return location;
    }

    protected GeoLocation getFromCache(String address) throws GeoException {
        String filename = getFileCacheName(address);
        File fileCache = new File(rootFolder, filename);
        if (fileCache.exists()) {
            try {
                FileReader in = new FileReader(fileCache);
                BufferedReader reader = new BufferedReader(in);

                String line = reader.readLine();
                String[] split = line.split(",");

                GeoLocation location = new GeoLocation(
                        Double.parseDouble(split[0]),
                        Double.parseDouble(split[1]));

                in.close();

                return location;
            } catch (IOException ex) {
                return null; // Unable to read from cache, skip
            }
        }
        return null;
    }

    protected void setInCache(String address, GeoLocation location) throws GeoException {
        String filename = getFileCacheName(address);
        File fileCache = new File(rootFolder, filename);

        try {
            FileWriter out = new FileWriter(fileCache);
            BufferedWriter writer = new BufferedWriter(out);

            writer.write(location.getLatitude() + "," + location.getLongitude());

            writer.flush();
            writer.close();
            out.close();
        } catch (IOException ex) {
            throw new GeoException("Unable to write to cache", ex);
        }
    }

    protected String getFileCacheName(String address) throws GeoException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            byte[] hash = digest.digest(address.getBytes());
            return toHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new GeoException("Unable to hash address", ex);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (int b : bytes) {
            if (b < 0) {
                b += 256;
            }
            if (b < 16) {
                builder.append("0");
            }
            builder.append(Integer.toHexString(b));
        }

        return builder.toString();
    }

}
