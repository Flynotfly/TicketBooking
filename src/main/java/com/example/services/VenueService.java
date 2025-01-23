package com.example.services;

import com.example.entities.Venue;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
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
    
    public List<Venue> searchVenues(String query, LocalDate startDate, LocalDate endDate) {
        // Start your base JPQL
        StringBuilder jpql = new StringBuilder("SELECT v FROM Venue v WHERE 1=1");

        // Check if query is not empty
        if (query != null && !query.trim().isEmpty()) {
            jpql.append(" AND (LOWER(v.name) LIKE :query OR LOWER(v.place) LIKE :query)");
        }

        // Append conditions based on dates
        if (startDate != null) {
            jpql.append(" AND v.datetime >= :startDate");
        }
        if (endDate != null) {
            jpql.append(" AND v.datetime <= :endDate");
        }

        // Create the TypedQuery
        TypedQuery<Venue> typedQuery = em.createQuery(jpql.toString(), Venue.class);

        // Set parameters
        if (query != null && !query.trim().isEmpty()) {
            typedQuery.setParameter("query", "%" + query.toLowerCase() + "%");
        }

        if (startDate != null) {
            typedQuery.setParameter("startDate", startDate.atStartOfDay());
        }
        if (endDate != null) {
            typedQuery.setParameter("endDate", endDate.atTime(23, 59, 59));
        }

        return typedQuery.getResultList();
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
