package com.example.resources;

import com.example.entities.Venue;
import com.example.services.VenueService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Events API", description = "API for managing events (venues).")
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VenueResource {

    @EJB
    private VenueService venueService;

    @Operation(summary = "Get all events", description = "Fetches a list of all events.")
    @GET
    public Response getAllEvents() {
        List<Venue> venues = venueService.getAllVenues();
        if (venues.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(venues).build();
    }

    @Operation(summary = "Get event by ID", description = "Fetches a specific event by its ID.")
    @GET
    @Path("/{id}")
    public Response getEventById(
            @Parameter(description = "ID of the event to fetch", required = true)
            @PathParam("id") Long id) {
        Venue venue = venueService.getVenueById(id);
        if (venue == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
        }
        return Response.ok(venue).build();
    }
}
