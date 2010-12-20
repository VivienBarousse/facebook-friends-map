/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.fbapi;

/**
 *
 * @author viv
 */
public class FbException extends Exception {

    public FbException() {
        super();
    }

    public FbException(String msg) {
        super(msg);
    }

    public FbException(Throwable cause) {
        super(cause);
    }

    public FbException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
