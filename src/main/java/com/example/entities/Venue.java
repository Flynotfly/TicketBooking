/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "venue")
public class Venue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime datetime;

    @Column(nullable = false)
    private String place;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, name = "quantity_of_tickets")
    private int quantityOfTickets;

    @Column(nullable = false, name = "ticket_price")
    private double ticketPrice;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, name = "booked_tickets")
    private int bookedTickets = 0;
    
    @Column(nullable = false) // New column for venue name
    private String name;
    
    @Transient // Not persisted in the database
    private String formattedDateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public int getQuantityOfTickets() { return quantityOfTickets; }
    public void setQuantityOfTickets(int quantityOfTickets) { this.quantityOfTickets = quantityOfTickets; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getBookedTickets() { return bookedTickets; }
    public void setBookedTickets(int bookedTickets) { this.bookedTickets = bookedTickets; }
    
    public String getName() {
        return name; // Getter for the new name column
    }

    public void setName(String name) {
        this.name = name; // Setter for the new name column
    }
    
    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }
    
    // Free tickets calculation
    public Integer getFreeTickets() {
        return quantityOfTickets - bookedTickets;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venue)) {
            return false;
        }
        Venue other = (Venue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.entities.Venue[ id=" + id + " ]";
    }
    
}
