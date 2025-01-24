package com.example.beans;

import com.example.entities.Category;
import com.example.entities.Venue;
import com.example.services.CategoryService;
import com.example.services.VenueService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@RequestScoped
public class VenueBean {
    private String searchQuery;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId; // ID of the selected category
    private List<Venue> venues;
    private List<Category> categories; // List of all categories for selection
    private Long selectedVenueId; // ID of the selected venue
    private Venue selectedVenue;

    // Inject your VenueService here
    @Inject
    private VenueService venueService;
    
    @Inject
    private CategoryService categoryService;

    public VenueBean() {
        venues = new ArrayList<>(); // Initialize with an empty list
    }
    
    @PostConstruct
    public void init() {
        categories = categoryService.getAllCategories(); // Fetch categories after injection
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String venueIdParam = params.get("venueId");
        if (venueIdParam != null) {
            try {
                selectedVenueId = Long.parseLong(venueIdParam);
                loadVenueDetails();
            } catch (NumberFormatException e) {
                // Handle invalid venueId gracefully
                selectedVenue = null;
            }
        }
    }


    public void search() {
        venues = venueService.searchVenues(searchQuery, startDate, endDate, categoryId);
    }
    
    public void loadVenueDetails() {
        if (selectedVenueId != null) {
            selectedVenue = venueService.getVenueById(selectedVenueId);
        }
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
    
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    // Getters and Setters
    public Long getSelectedVenueId() {
        return selectedVenueId;
    }

    public void setSelectedVenueId(Long selectedVenueId) {
        this.selectedVenueId = selectedVenueId;
    }

    public Venue getSelectedVenue() {
        return selectedVenue;
    }

    public void setSelectedVenue(Venue selectedVenue) {
        this.selectedVenue = selectedVenue;
    }
}
