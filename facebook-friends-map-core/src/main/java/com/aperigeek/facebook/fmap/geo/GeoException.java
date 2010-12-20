/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.geo;

/**
 *
 * @author viv
 */
public class GeoException extends Exception {

    public GeoException() {
    }

    public GeoException(String msg) {
        super(msg);
    }

    public GeoException(Throwable cause) {
        super(cause);
    }

    public GeoException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
