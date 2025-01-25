/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.beans;

import com.example.entities.Booking;
import com.example.services.BookingService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.util.List;

@Named
@RequestScoped
public class BookingBean {

    private List<Booking> userBookings;

    @Inject
    private BookingService bookingService;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        if (loginBean.getLoggedInUser() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            try {
                context.getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Long userId = loginBean.getLoggedInUser().getId();
            userBookings = bookingService.getBookingsByUserId(userId);
        }
    }


    // Getters and Setters
    public List<Booking> getUserBookings() {
        return userBookings;
    }

    public void setUserBookings(List<Booking> userBookings) {
        this.userBookings = userBookings;
    }
}
