package com.example.beans;

import com.example.entities.Category;
import com.example.entities.Venue;
import com.example.services.CategoryService;
import com.example.services.VenueService;
import com.example.services.BookingService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
    private int numberOfTickets;
    private String bookingMessage;
    
    private static final Logger LOGGER = Logger.getLogger(VenueBean.class.getName());

    // Inject your VenueService here
    @Inject
    private VenueService venueService;
    
    @Inject
    private CategoryService categoryService;
    
    @Inject
    private BookingService bookingService;
    
    @Inject
    private LoginBean loginBean;

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
    
    public String bookTickets() {
        LOGGER.warning("Start booking");
        Long userId = loginBean.getLoggedInUser().getId();
        LOGGER.warning("Logged user start book ticket. UserId: " + userId + " selected venue: " + selectedVenue);
        if (selectedVenue == null || userId == null) {
            bookingMessage = "Failed to book tickets. Please try again.";
            LOGGER.warning("Logged user start book ticket and get error");
            return null;
        }
        LOGGER.warning("Logged user start book ticket and continue");
        boolean success = bookingService.bookTickets(selectedVenueId, userId, numberOfTickets);
        if (success) {
            bookingMessage = "Tickets booked successfully!";
            loadVenueDetails(); // Refresh venue details to update free tickets
            return "venueDetails.xhtml?faces-redirect=true&venueId=" + selectedVenueId;
        } else {
            bookingMessage = "Not enough tickets available.";
        }
        return null;
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
    
    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public String getBookingMessage() {
        return bookingMessage;
    }

    public void setBookingMessage(String bookingMessage) {
        this.bookingMessage = bookingMessage;
    }
}
