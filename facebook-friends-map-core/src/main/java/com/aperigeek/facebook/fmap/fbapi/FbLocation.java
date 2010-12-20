/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aperigeek.facebook.fmap.fbapi;

/**
 *
 * @author viv
 */
public class FbLocation {

    private long id;

    private String name;

    public FbLocation(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FbLocation other = (FbLocation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "FbLocation{" + "id=" + id + ";name=" + name + '}';
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
