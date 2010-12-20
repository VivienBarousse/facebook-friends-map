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
 * @author Vivien Barousse
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
