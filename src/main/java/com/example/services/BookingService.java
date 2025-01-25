package com.example.services;

import com.example.entities.Booking;
import com.example.entities.User;
import com.example.entities.Venue;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BookingService {
    @PersistenceContext(unitName = "bookingPU")
    private EntityManager em;
    
    @Inject
    private VenueService venueService;

    public void addBooking(Booking booking) {
        em.persist(booking);
    }

    public List<Booking> getAllBookings() {
        return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    public Booking getBookingById(Long id) {
        return em.find(Booking.class, id);
    }

    public void deleteBooking(Long id) {
        Booking booking = em.find(Booking.class, id);
        if (booking != null) {
            em.remove(booking);
        }
    }
    
    public List<Booking> getBookingsByUser(User user) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.user = :user", Booking.class)
                 .setParameter("user", user)
                 .getResultList();
    }
    
    public List<Booking> getBookingsByUserId(Long userId) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.user.id = :userId", Booking.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }
    
    public boolean bookTickets(Long venueId, Long userId, int numberOfTickets) {
        Venue venue = venueService.getVenueById(venueId);

        if (venue == null) {
            throw new IllegalArgumentException("Venue not found");
        }

        if (venue.getFreeTickets() < numberOfTickets) {
            return false; // Not enough tickets available
        }

        venue.setBookedTickets(venue.getBookedTickets() + numberOfTickets);
        em.merge(venue);

        Booking booking = new Booking();
        booking.setVenue(venue);
        booking.setUser(em.find(User.class, userId));
        booking.setQuantity(numberOfTickets);
        em.persist(booking);

        return true;
    }

}
