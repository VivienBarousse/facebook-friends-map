/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author viv
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
