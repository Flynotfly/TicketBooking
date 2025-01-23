package com.example.resources;

import com.example.entities.Booking;
import com.example.entities.User;
import com.example.entities.Venue;
import com.example.services.BookingService;
import com.example.services.UserService;
import com.example.services.VenueService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.container.ContainerRequestContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Booking API", description = "APIs for managing user bookings.")
public class BookingResource {

    @EJB
    private BookingService bookingService;

    @EJB
    private UserService userService;

    @EJB
    private VenueService venueService;

    @Operation(summary = "Create a booking", description = "Allows a user to create a booking for an event.")
    @POST
    public Response createBooking(
            @Context ContainerRequestContext requestContext,
            @Parameter(description = "Booking request payload", required = true) Booking bookingRequest) {
        Long userId = (Long) requestContext.getProperty("userId");

        User user = userService.findUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Venue venue = venueService.getVenueById(bookingRequest.getVenue().getId());
        if (venue == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Venue not found").build();
        }

        int availableTickets = venue.getQuantityOfTickets() - venue.getBookedTickets();
        if (bookingRequest.getQuantity() > availableTickets) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Not enough free tickets. Available: " + availableTickets)
                    .build();
        }

        venue.setBookedTickets(venue.getBookedTickets() + bookingRequest.getQuantity());
        venueService.updateVenue(venue);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVenue(venue);
        booking.setQuantity(bookingRequest.getQuantity());
        bookingService.addBooking(booking);

        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @Operation(summary = "Get all bookings", description = "Fetches all bookings for the authenticated user.")
    @GET
    public Response getBookings(@Context ContainerRequestContext requestContext) {
        // Get the authenticated user ID from the request context
        Long userId = (Long) requestContext.getProperty("userId");

        // Find the authenticated user
        User user = userService.findUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // Check if the user is an admin
        if (user.getRole() == User.Role.ADMIN) {
            // If admin, fetch all bookings
            return Response.ok(bookingService.getAllBookings()).build();
        } else {
            // If not admin, fetch only bookings for the specific user
            return Response.ok(bookingService.getBookingsByUser(user)).build();
        }
    }

    @Operation(summary = "Delete a booking", description = "Allows a user to delete their own booking.")
    @DELETE
    @Path("/{id}")
    public Response deleteBooking(
            @Context ContainerRequestContext requestContext,
            @Parameter(description = "ID of the booking to delete", required = true) @PathParam("id") Long bookingId) {
        Long userId = (Long) requestContext.getProperty("userId");

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
        }

        if (!booking.getUser().getId().equals(userId)) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not authorized to delete this booking").build();
        }

        bookingService.deleteBooking(bookingId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
