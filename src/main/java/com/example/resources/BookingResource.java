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

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    @EJB
    private BookingService bookingService;

    @EJB
    private UserService userService;

    @EJB
    private VenueService venueService;

    @POST
    public Response createBooking(@Context ContainerRequestContext requestContext, Booking bookingRequest) {
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

        // Update venue's booked tickets
        venue.setBookedTickets(venue.getBookedTickets() + bookingRequest.getQuantity());
        venueService.updateVenue(venue);

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVenue(venue);
        booking.setQuantity(bookingRequest.getQuantity());
        bookingService.addBooking(booking);

        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @GET
    public Response getBookings(@Context ContainerRequestContext requestContext) {
        Long userId = (Long) requestContext.getProperty("userId");

        User user = userService.findUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(bookingService.getBookingsByUser(user)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@Context ContainerRequestContext requestContext, @PathParam("id") Long bookingId) {
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
