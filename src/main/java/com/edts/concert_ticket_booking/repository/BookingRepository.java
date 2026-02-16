package com.edts.concert_ticket_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edts.concert_ticket_booking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByConcertIdAndUserId(Long concertId, String userId);
    
}
