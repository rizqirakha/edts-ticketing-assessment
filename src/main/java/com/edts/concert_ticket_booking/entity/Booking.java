package com.edts.concert_ticket_booking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"concert_id", "user_id"})
})
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    private String userId;
    private String bookingId;
    private LocalDateTime bookingTime;
}
