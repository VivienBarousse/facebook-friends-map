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
 * @author viv
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
