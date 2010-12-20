/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.geo;

/**
 *
 * @author viv
 */
public interface GeocodingClient {

    public GeoLocation geocode(String address) throws GeoException;

}
