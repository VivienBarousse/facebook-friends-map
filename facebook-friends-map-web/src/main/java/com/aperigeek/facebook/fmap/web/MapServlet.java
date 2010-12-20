/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aperigeek.facebook.fmap.web;

import com.aperigeek.facebook.fmap.CachedFbMapService;
import com.aperigeek.facebook.fmap.FbMapService;
import com.aperigeek.facebook.fmap.FbMapServiceImpl;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
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
@WebServlet(name="MapServlet", urlPatterns={"/map.png"})
public class MapServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String accessToken = (String) session.getAttribute("fb_access_token");

        FbMapService service = new CachedFbMapService(new FbMapServiceImpl());
        try {
            BufferedImage image = service.generateMap(accessToken);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);
        } catch (Exception ex) {
            throw new ServletException("Unexpected error while generating map", ex);
        }
    } 

}
