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
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vivien Barousse
 */
@WebServlet(name="LoginCallbackServlet", urlPatterns={"/login_callback"})
public class LoginCallbackServlet extends HttpServlet {

    private long clientId;

    private String clientSecret;

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
        try {
            InputStream in = this.getClass().getResourceAsStream("/api_secret");
            Reader inr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inr);

            clientSecret = reader.readLine();

            in.close();
        } catch (IOException ex) {
            throw new ServletException("Unable to initialize servlet", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fburl = "https://graph.facebook.com/oauth/access_token";
        fburl += "?client_id=" + clientId;
        fburl += "&redirect_uri=" + URLEncoder.encode(getServerUrl(request) + "login_callback", "UTF-8");
        fburl += "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8");
        fburl += "&code=" + URLEncoder.encode(request.getParameter("code"), "UTF-8");

        URL url = new URL(fburl);
        InputStream in = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String readed = reader.readLine();

        String accessToken = null;

        String[] args = readed.split("&");
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split[0].equals("access_token")) {
                accessToken = split[1];
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute("fb_access_token", accessToken);

        response.sendRedirect("map.jsp");

        in.close();
    }

    protected String getServerUrl(HttpServletRequest req) {
        StringBuffer buf = req.getRequestURL();
        int length = buf.length();
        buf.delete(length - 14, length);
        return buf.toString();
    }

}
