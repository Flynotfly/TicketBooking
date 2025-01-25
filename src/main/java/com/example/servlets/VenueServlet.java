package com.example.servlets;

import com.example.services.VenueService;
import com.example.entities.Venue;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/venues")
public class VenueServlet extends HttpServlet {

    @Inject
    private VenueService venueService;
    
    private static final Logger LOGGER = Logger.getLogger(VenueServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Venue> venues = venueService.getAllVenues(); // Ensure this returns a list
        LOGGER.warning("Find venues for JSP: " + venues.size());
        request.setAttribute("venues", venues); // Set the list as a request attribute
        request.getRequestDispatcher("/venues.jsp").forward(request, response); // Forward to JSP
    }
}
