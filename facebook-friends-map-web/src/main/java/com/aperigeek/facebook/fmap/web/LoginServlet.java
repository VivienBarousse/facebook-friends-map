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

package com.aperigeek.facebook.fmap.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vivien Barousse
 */
@WebServlet(name="LoginServlet", urlPatterns={"/login"})
public class LoginServlet extends HttpServlet {

    private long clientId;

    @Override
    public void init() throws ServletException {
        try {
            InputStream in = this.getClass().getResourceAsStream("/api_id");
            Reader inr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inr);

            clientId = Long.parseLong(reader.readLine());

            in.close();
        } catch (IOException ex) {
            throw new ServletException("Unable to initialize servlet", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fburl = "https://graph.facebook.com/oauth/authorize";
        fburl += "?client_id=" + clientId;
        fburl += "&redirect_uri=" + URLEncoder.encode(getServerUrl(request) + "login_callback", "UTF-8");
        fburl += "&scope=friends_location";
        response.sendRedirect(fburl);
    }

    protected String getServerUrl(HttpServletRequest req) {
        StringBuffer buf = req.getRequestURL();
        int length = buf.length();
        buf.delete(length - 5, length);
        return buf.toString();
    }

}
