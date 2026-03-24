package org.example.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.entity.Booking;
import org.example.bookingservice.entity.BookingStatus;
import org.example.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BookingServiceApplicationTests {

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // Verifies Spring context starts correctly
    }

    @Test
    void shouldSaveAndRetrieveBooking() {
        Booking booking = Booking.builder()
                .rideId(1L)
                .passengerId("user-123")
                .passengerEmail("test@ecoride.com")
                .seatsBooked(2)
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void shouldFindBookingsByPassenger() {
        bookingRepository.save(Booking.builder()
                .rideId(10L)
                .passengerId("user-abc")
                .seatsBooked(1)
                .status(BookingStatus.CONFIRMED)
                .build());

        bookingRepository.save(Booking.builder()
                .rideId(20L)
                .passengerId("user-abc")
                .seatsBooked(1)
                .status(BookingStatus.PENDING)
                .build());

        List<Booking> results = bookingRepository.findByPassengerId("user-abc");
        assertThat(results).hasSize(2);
    }

    @Test
    void shouldFindBookingsByStatus() {
        bookingRepository.save(Booking.builder()
                .rideId(1L)
                .passengerId("user-1")
                .seatsBooked(1)
                .status(BookingStatus.CANCELLED)
                .build());

        List<Booking> cancelled = bookingRepository.findByStatus(BookingStatus.CANCELLED);
        assertThat(cancelled).isNotEmpty();
        assertThat(cancelled.get(0).getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }
}

