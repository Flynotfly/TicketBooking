package com.example.beans;

import com.example.entities.Venue;
import com.example.services.VenueService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class VenueBean {
    private String searchQuery;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Venue> venues;

    // Inject your VenueService here
    @Inject
    private VenueService venueService;

    public VenueBean() {
        venues = new ArrayList<>(); // Initialize with an empty list
    }

    public void search() {
        venues = venueService.searchVenues(searchQuery, startDate, endDate);
    }

    // Getters and Setters
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
