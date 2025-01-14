package com.example.services;

import com.example.entities.Booking;
import com.example.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BookingService {
    @PersistenceContext(unitName = "bookingPU")
    private EntityManager em;

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

}
