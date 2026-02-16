package com.edts.concert_ticket_booking.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.edts.concert_ticket_booking.dto.BookingDto;
import com.edts.concert_ticket_booking.dto.ConcertDto;
import com.edts.concert_ticket_booking.entity.Booking;
import com.edts.concert_ticket_booking.exception.BusinessException;
import com.edts.concert_ticket_booking.repository.BookingRepository;
import com.edts.concert_ticket_booking.repository.ConcertRepository;

import jakarta.transaction.Transactional;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final BookingRepository bookingRepository;
    private static final SecureRandom random = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(ConcertService.class);

    public ConcertService(ConcertRepository concertRepository, BookingRepository bookingRepository) {
        this.concertRepository = concertRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ConcertDto> getAllConcerts() {
        List<ConcertDto> concertsDto = new ArrayList<>();
        var concerts = concertRepository.findAll();
        for (var concert : concerts) {
            ConcertDto dto = new ConcertDto();
            dto.setId(concert.getId());
            dto.setName(concert.getName());
            dto.setAvailableTickets(concert.getAvailableTickets());
            dto.setReservationStart(concert.getReservationStart());
            dto.setReservationEnd(concert.getReservationEnd());
            concertsDto.add(dto);
        }
        return concertsDto;
    }

    @Transactional
    public BookingDto bookConcert(Long concertId, String userId) {
        var requestId = MDC.get("requestId");

        if (bookingRepository.existsByConcertIdAndUserId(concertId, userId)) {
            log.warn("User {} has already booked a ticket for concertId: {}", requestId, userId, concertId);
            throw new BusinessException(HttpStatus.CONFLICT, "ALREADY_BOOKED",
                    "User has already booked a ticket for this concert");
        }

        var concertOpt = concertRepository.findById(concertId);
        var concert = concertOpt.orElseThrow(
                () -> {
                    log.warn("Concert not found for ID: {}", concertId);
                    return new BusinessException(HttpStatus.NOT_FOUND, "CONCERT_NOT_FOUND", "Concert not found");
                });

        if (!concert.isReservationOpen()) {
            log.warn("Reservations are not open for concertId: {}", concertId);
            throw new BusinessException(HttpStatus.BAD_REQUEST, "RESERVATION_NOT_OPEN",
                    "Reservations are not open for this concert");
        }

        try {
            concert.bookTicket();
        } catch (RuntimeException e) {
            log.warn("No tickets available for concertId: {}", concertId);
            throw new BusinessException(HttpStatus.CONFLICT, "NO_TICKETS_AVAILABLE",
                    "No tickets available for this concert");
        }

        Booking booking = new Booking();
        booking.setConcert(concert);
        booking.setUserId(userId);
        booking.setBookingId(generateBookingId(concert.getName()));
        booking.setBookingTime(LocalDateTime.now());
        bookingRepository.save(booking);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(booking.getBookingId());
        bookingDto.setConcertName(booking.getConcert().getName());
        bookingDto.setUserId(booking.getUserId());
        bookingDto.setBookingTime(booking.getBookingTime().toString());

        log.info("Booking successful for concertId: {}, userId: {}, bookingId: {}",
                concertId, userId, booking.getBookingId());
        return bookingDto;
    }

    private String extract3Letters(String name) {
        String extracedString = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (extracedString.isEmpty())
            return "BOK";
        return extracedString.substring(0, Math.min(3, extracedString.length()));
    }

    private String generateBookingId(String concertName) {
        concertName = extract3Letters(concertName);
        int number = random.nextInt(900_000) + 100_000;
        return concertName + "-" + number;
    }
}
