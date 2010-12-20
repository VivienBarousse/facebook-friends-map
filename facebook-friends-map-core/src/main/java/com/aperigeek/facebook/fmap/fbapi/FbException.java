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

/**
 *
 * @author Vivien Barousse
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
