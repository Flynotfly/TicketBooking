package com.example.resources;

import com.example.entities.Venue;
import com.example.services.VenueService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VenueResource {

    @EJB
    private VenueService venueService;

    @GET
    public Response getAllEvents() {
        List<Venue> venues = venueService.getAllVenues();
        if (venues.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(venues).build();
    }

    @GET
    @Path("/{id}")
    public Response getEventById(@PathParam("id") Long id) {
        Venue venue = venueService.getVenueById(id);
        if (venue == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        }
        return Response.ok(venue).build();
    }
}
