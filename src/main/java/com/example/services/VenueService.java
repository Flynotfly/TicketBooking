package com.example.services;

import com.example.entities.Venue;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class VenueService {
    @PersistenceContext(unitName = "bookingPU")
    private EntityManager em;

    public void addVenue(Venue venue) {
        em.persist(venue);
    }

    public List<Venue> getAllVenues() {
        return em.createQuery("SELECT v FROM Venue v", Venue.class).getResultList();
    }
    
    public List<Venue> searchVenues(String query) {
        return em.createQuery("SELECT v FROM Venue v WHERE LOWER(v.name) LIKE :query", Venue.class)
                 .setParameter("query", "%" + query.toLowerCase() + "%")
                 .getResultList();
    }


    public Venue getVenueById(Long id) {
        return em.find(Venue.class, id);
    }

    public void updateVenue(Venue venue) {
        em.merge(venue);
    }

    public void deleteVenue(Long id) {
        Venue venue = em.find(Venue.class, id);
        if (venue != null) {
            em.remove(venue);
        }
    }
}
