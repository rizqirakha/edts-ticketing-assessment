package com.edts.concert_ticket_booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.edts.concert_ticket_booking.entity.Concert;
import jakarta.persistence.LockModeType;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Concert> findById(Long id);
}
