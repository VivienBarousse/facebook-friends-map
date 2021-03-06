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

package com.aperigeek.facebook.fmap.fbapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Vivien Barousse
 */
public class FbClientImpl implements FbClient {

    public static final String FB_CHARSET = "UTF-8";

    private String accessToken;

    public FbClientImpl(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getUserId() throws FbException {
        try {
            String fburl = "https://graph.facebook.com/me?access_token="
                    + URLEncoder.encode(accessToken, FB_CHARSET);

            URL url = new URL(fburl);

            InputStream urlIn = url.openStream();
            InputStreamReader reader = new InputStreamReader(urlIn);

            JSONTokener tokener = new JSONTokener(reader);
            JSONObject me = (JSONObject) tokener.nextValue();

            urlIn.close();

            return me.getLong("id");
        } catch (IOException ex) {
            throw new FbException("Unsupported I/O error", ex);
        } catch (JSONException ex) {
            throw new FbException("API error: invlaid JSON", ex);
        }
    }

    public FbLocation getCurrentLocation() throws FbException {
        try {
            String fburl = "https://graph.facebook.com/me?access_token="
                    + URLEncoder.encode(accessToken, FB_CHARSET);

            URL url = new URL(fburl);

            InputStream urlIn = url.openStream();
            InputStreamReader reader = new InputStreamReader(urlIn);

            JSONTokener tokener = new JSONTokener(reader);
            JSONObject me = (JSONObject) tokener.nextValue();

            if (!me.has("location")) {
                return null;
            }

            JSONObject jsonLocation = me.getJSONObject("location");
            FbLocation location = new FbLocation(
                    jsonLocation.getLong("id"),
                    jsonLocation.getString("name"));

            urlIn.close();

            return location;
        } catch (IOException ex) {
            throw new FbException("Unsupported I/O error", ex);
        } catch (JSONException ex) {
            throw new FbException("API error: invlaid JSON", ex);
        }
    }

    public List<FbFriend> getFriends() throws FbException {
        try {
            String fburl = "https://graph.facebook.com/me/friends?access_token="
                    + URLEncoder.encode(accessToken, FB_CHARSET);

            URL url = new URL(fburl);

            InputStream urlIn = url.openStream();
            InputStreamReader reader = new InputStreamReader(urlIn);

            JSONTokener tokener = new JSONTokener(reader);

            JSONObject object = (JSONObject) tokener.nextValue();
            JSONArray data = object.getJSONArray("data");

            List<FbFriend> friends = new ArrayList<FbFriend>(data.length());
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonFriend = data.getJSONObject(i);
                friends.add(new FbFriend(
                        jsonFriend.getLong("id"),
                        jsonFriend.getString("name")));
            }

            urlIn.close();

            return friends;
        } catch (IOException ex) {
            throw new FbException("Unsupported I/O error", ex);
        } catch (JSONException ex) {
            throw new FbException("API error: invlaid JSON", ex);
        }
    }

    public FbLocation getFriendLocation(FbFriend friend) throws FbException {
        try {
            String fburl = "https://graph.facebook.com/" + friend.getId()
                    + "?access_token=" + URLEncoder.encode(accessToken, FB_CHARSET);

            URL url = new URL(fburl);

            InputStream urlIn = url.openStream();
            InputStreamReader reader = new InputStreamReader(urlIn);

            JSONTokener tokener = new JSONTokener(reader);

            JSONObject jsonFriend = (JSONObject) tokener.nextValue();

            FbLocation location = null;

            if (jsonFriend.has("location")) {
                JSONObject jsonLocation = jsonFriend.getJSONObject("location");
                location = new FbLocation(
                        jsonLocation.getLong("id"),
                        jsonLocation.getString("name"));
            }

            urlIn.close();

            return location;
        } catch (IOException ex) {
            throw new FbException("Unexpected I/O error", ex);
        } catch (JSONException ex) {
            throw new FbException("API error: invalid JSON", ex);
        }
    }

}
