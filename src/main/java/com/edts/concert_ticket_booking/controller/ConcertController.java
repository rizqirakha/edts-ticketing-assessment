package com.edts.concert_ticket_booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edts.concert_ticket_booking.api.ApiResponse;
import com.edts.concert_ticket_booking.dto.BookingDto;
import com.edts.concert_ticket_booking.dto.ConcertDto;
import com.edts.concert_ticket_booking.services.ConcertService;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    private static final Logger log = LoggerFactory.getLogger(ConcertController.class);
    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping
    public ApiResponse<List<ConcertDto>> listConcerts() {
        log.info("Received request to list concerts");
        List<ConcertDto> getConcerts = concertService.getAllConcerts();
        String message = getConcerts.isEmpty()
                ? "No concerts available"
                : "Concerts retrieved successfully";

        return ApiResponse.success(message, getConcerts);
    }

    @PostMapping("/book")
    public ApiResponse<BookingDto> bookConcert(@RequestBody Map<String, String> body) {
        String requestId = MDC.get("requestId");
        log.info("Received booking request, requestId: {}, body: {}", requestId, body);
        Long concertId = Long.parseLong(body.get("concertId"));
        BookingDto bookingDto = concertService.bookConcert(concertId, body.get("userId"));

        return ApiResponse.success(
                "Reservation successful",
                bookingDto);
    }
}
