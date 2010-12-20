/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aperigeek.facebook.fmap.fbapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author viv
 */
public class CachedFbClient implements FbClient {

    private File rootFolder;

    private FbClient wrapped;

    public CachedFbClient(File rootFolder, FbClient wrapped) {
        this.rootFolder = rootFolder;
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        this.wrapped = wrapped;
    }

    public long getUserId() throws FbException {
        return wrapped.getUserId();
    }

    public FbLocation getCurrentLocation() throws FbException {
        return wrapped.getCurrentLocation();
    }

    public List<FbFriend> getFriends() throws FbException {
        return wrapped.getFriends();
    }

    public FbLocation getFriendLocation(FbFriend friend) throws FbException {
        FbLocation location = getFromCache(friend);
        if (location == null) {
            location = wrapped.getFriendLocation(friend);
            storeInCache(friend, location);
        }
        return location;
    }

    public FbLocation getFromCache(FbFriend friend) {
        try {
            File file = new File(rootFolder, Long.toString(friend.getId()));
            if (!file.exists()) {
                return null;
            }
            FileReader in = new FileReader(file);
            BufferedReader reader = new BufferedReader(in);

            String id = reader.readLine();
            if (id.equals("nolocation")) {
                return null;
            }
            String name = reader.readLine();

            return new FbLocation(Long.parseLong(id), name);
        } catch (IOException ex) {
            return null; // Unable to read from cache, continue
        }
    }

    public void storeInCache(FbFriend friend, FbLocation location) throws FbException {
        try {
            File file = new File(rootFolder, Long.toString(friend.getId()));
            PrintWriter writer = new PrintWriter(file);

            if (location == null) {
                writer.println("nolocation");
            } else {
                writer.println(location.getId());
                writer.println(location.getName());
            }

            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new FbException("Unable to write to cache", ex);
        }
    }

}
