package com.edts.concert_ticket_booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {
    private String bookingId;
    private String concertName;
    private String userId;
    private String bookingTime;
}
