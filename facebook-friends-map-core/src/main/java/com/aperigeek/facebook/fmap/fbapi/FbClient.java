/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aperigeek.facebook.fmap.fbapi;

import java.util.List;

/**
 *
 * @author viv
 */
public interface FbClient {

    public long getUserId() throws FbException;

    public FbLocation getCurrentLocation() throws FbException;

    public List<FbFriend> getFriends() throws FbException;

    public FbLocation getFriendLocation(FbFriend friend) throws FbException;

}
