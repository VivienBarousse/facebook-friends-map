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
 * @author Vivien Barousse
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
