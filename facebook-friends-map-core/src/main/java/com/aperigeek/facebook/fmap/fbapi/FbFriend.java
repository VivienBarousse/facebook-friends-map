/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.fbapi;

/**
 *
 * @author viv
 */
public class FbFriend {

    private long id;

    private String name;

    public FbFriend(long id, String name) {
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
        final FbFriend other = (FbFriend) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "FbFriend{" + "id=" + id + ";name=" + name + '}';
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
