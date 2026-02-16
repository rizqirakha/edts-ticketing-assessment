package com.edts.concert_ticket_booking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Concert {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int totalTickets;
    private int availableTickets;
    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;

    @Version
    private Long version;

    private LocalDateTime updatedAt;
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public boolean isReservationOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(reservationStart) && now.isBefore(reservationEnd);
    }

    public void bookTicket() {
        if (availableTickets <= 0)
            throw new RuntimeException("No tickets available");
        availableTickets--;
    }
}
