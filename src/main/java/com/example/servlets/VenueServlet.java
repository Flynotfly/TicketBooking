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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/venues")
public class VenueServlet extends HttpServlet {

    @Inject
    private VenueService venueService;
    
    private static final Logger LOGGER = Logger.getLogger(VenueServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch venues from the service
        List<Venue> venues = venueService.getAllVenues();

        // Format LocalDateTime to a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        venues.forEach(venue -> {
            if (venue.getDatetime() != null) {
                venue.setFormattedDateTime(venue.getDatetime().format(formatter));
            }
        });

        // Pass venues to the JSP
        request.setAttribute("venues", venues);
        request.getRequestDispatcher("/venues.jsp").forward(request, response);
    }
}
