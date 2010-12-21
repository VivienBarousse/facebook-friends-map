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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vivien Barousse
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
        FbLocation location;
        try {
            location = getFromCache(friend);
        } catch (NotInCacheException ex) {
            location = wrapped.getFriendLocation(friend);
            storeInCache(friend, location);
        }
        return location;
    }

    public FbLocation getFromCache(FbFriend friend) throws NotInCacheException {
        try {
            File file = new File(rootFolder, Long.toString(friend.getId()));
            if (!file.exists()) {
                throw new NotInCacheException();
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
            throw new NotInCacheException();
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

    protected static class NotInCacheException extends Exception {
        
    }

}
