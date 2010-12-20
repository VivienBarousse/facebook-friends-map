/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.gmapi;

import com.aperigeek.facebook.fmap.geo.GeoException;

/**
 *
 * @author viv
 */
public class GMapsException extends GeoException {

    public GMapsException() {
    }

    public GMapsException(String msg) {
        super(msg);
    }

    public GMapsException(Throwable cause) {
        super(cause);
    }

    public GMapsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
