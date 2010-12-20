/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aperigeek.facebook.fmap.geo.osm;

import com.aperigeek.facebook.fmap.geo.GeoException;
import com.aperigeek.facebook.fmap.geo.GeoLocation;
import com.aperigeek.facebook.fmap.geo.GeocodingClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author viv
 */
public class NominatimGeocodingClient implements GeocodingClient {

    public static final String NOMINATIM_CHARSET = "UTF-8";

    public GeoLocation geocode(String address) throws GeoException {
        try {
            String osmurl = "http://nominatim.openstreetmap.org/search?format=json&q="
                    + URLEncoder.encode(address, NOMINATIM_CHARSET);

            URL url = new URL(osmurl);

            InputStream in = url.openStream();
            InputStreamReader reader = new InputStreamReader(in);
            JSONTokener tokener = new JSONTokener(reader);

            JSONArray results = (JSONArray) tokener.nextValue();

            if (results.length() == 0) {
                System.out.println("No results for: " + address);
                return null;
            }

            JSONObject result = results.getJSONObject(0);

            return new GeoLocation(
                    result.getDouble("lat"),
                    result.getDouble("lon"));
        } catch (IOException ex) {
            throw new GeoException("Unexpected I/O error", ex);
        } catch (JSONException ex) {
            throw new GeoException("API error: invalid JSON message", ex);
        }
    }

}
