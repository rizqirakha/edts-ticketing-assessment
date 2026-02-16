package com.edts.concert_ticket_booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.edts.concert_ticket_booking.dto.ConcertDto;
import com.edts.concert_ticket_booking.entity.Concert;
import com.edts.concert_ticket_booking.exception.BusinessException;
import com.edts.concert_ticket_booking.repository.BookingRepository;
import com.edts.concert_ticket_booking.repository.ConcertRepository;
import com.edts.concert_ticket_booking.services.ConcertService;

@SpringBootTest
class ConcertServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private final String user = "test1"; // Define the user

    @Test
    void getAllConcertsService() {
        Concert concert1 = new Concert();
        concert1.setId(1L);
        concert1.setName("Dewa 19");
        concert1.setAvailableTickets(100);

        Concert concert2 = new Concert();
        concert2.setId(2L);
        concert2.setName("Coldplay");
        concert2.setAvailableTickets(50);

        when(concertRepository.findAll())
                .thenReturn(List.of(concert1, concert2));

        // when
        List<ConcertDto> result = concertService.getAllConcerts();

        // then
        assertEquals(2, result.size());

        assertEquals("Dewa 19", result.get(0).getName());
        assertEquals(100, result.get(0).getAvailableTickets());

        assertEquals("Coldplay", result.get(1).getName());
        assertEquals(50, result.get(1).getAvailableTickets());
    }

    @Test
    void bookConcertService() {
        Concert concert = new Concert();
        concert.setId(1L);
        concert.setName("Coldplay");
        concert.setAvailableTickets(10);
        concert.setReservationStart(LocalDateTime.now().minusHours(1));
        concert.setReservationEnd(LocalDateTime.now().plusHours(1));

        when(bookingRepository.existsByConcertIdAndUserId(1L, user))
                .thenReturn(false);

        when(concertRepository.findById(1L))
                .thenReturn(Optional.of(concert));

        var result = concertService.bookConcert(1L, user);

        assertNotNull(result);
        assertEquals(user, result.getUserId());
    }

    @Test
    void bookConcertThrowUserAlreadyBookedConcert() {
        Long concertId = 1L;

        when(bookingRepository.existsByConcertIdAndUserId(concertId, user))
                .thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> concertService.bookConcert(concertId, user));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("ALREADY_BOOKED", exception.getErrorCode());
        assertEquals("User has already booked a ticket for this concert", exception.getMessage());

        verify(concertRepository, never()).findById(any());
    }

    @Test
    void bookConcertThrowDuplicateBooking() {
        when(bookingRepository.existsByConcertIdAndUserId(1L, user))
                .thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> concertService.bookConcert(1L, user));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void bookConcertThrowConcertNotFound() {
        var concertId = 1L;
        assertThrows(
                BusinessException.class,
                () -> concertService.bookConcert(concertId, user));
    }

    @Test
    void bookConcertThrowReservationNotOpen() {
        Concert concert = new Concert();
        concert.setId(1L);
        concert.setName("Coldplay");
        concert.setAvailableTickets(10);
        concert.setReservationStart(LocalDateTime.now().plusHours(1)); // Reservation starts in the future
        concert.setReservationEnd(LocalDateTime.now().plusHours(2));

        when(bookingRepository.existsByConcertIdAndUserId(1L, user))
                .thenReturn(false);

        when(concertRepository.findById(1L))
                .thenReturn(Optional.of(concert));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> concertService.bookConcert(1L, user));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

}
