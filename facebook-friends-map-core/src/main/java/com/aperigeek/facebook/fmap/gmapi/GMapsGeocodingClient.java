/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.gmapi;

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
public class GMapsGeocodingClient implements GeocodingClient {

    public static final String GMAPS_CHARSET = "UTF-8";

    public GeoLocation geocode(String address) throws GMapsException {
        try {
            String gmurl = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address="
                    + URLEncoder.encode(address, GMAPS_CHARSET);

            URL url = new URL(gmurl);

            InputStream in = url.openStream();
            InputStreamReader reader = new InputStreamReader(in);

            JSONTokener tokener = new JSONTokener(reader);

            JSONObject object = (JSONObject) tokener.nextValue();
            String status = object.getString("status");
            if (status.equals("ZERO_RESULTS")) {
                return null;
            }
            if (!status.equals("OK")) {
                throw new GMapsException("Error during request: " + status);
            }

            JSONArray results = object.getJSONArray("results");
            JSONObject result = results.getJSONObject(0);
            JSONObject geometry = result.getJSONObject("geometry");
            JSONObject jsonLocation = geometry.getJSONObject("location");

            GeoLocation location = new GeoLocation(
                    jsonLocation.getDouble("lat"),
                    jsonLocation.getDouble("lng"));

            in.close();

            return location;
        } catch (IOException ex) {
            throw new GMapsException("Unexpectec I/O error", ex);
        } catch (JSONException ex) {
            throw new GMapsException("API error: invalid JSON", ex);
        }
    }

}
