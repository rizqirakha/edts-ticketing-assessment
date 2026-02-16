package com.edts.concert_ticket_booking.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConcertDto {
    private Long id;
    private String name;
    private int totalTickets;
    private int availableTickets;
    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;
}
