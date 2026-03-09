package org.example.bookingservice.controller;

import jakarta.validation.Valid;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.entity.BookingStatus;
import org.example.bookingservice.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * POST /api/bookings — Passenger creates a booking
     */
    @PostMapping
    @PreAuthorize("hasRole('PASSAGER') or hasRole('PASSENGER')")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(request, auth));
    }

    /**
     * GET /api/bookings/my — Get current user's bookings
     */
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication auth) {
        return ResponseEntity.ok(bookingService.getMyBookings(auth));
    }

    /**
     * GET /api/bookings/{id} — Get a single booking by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(bookingService.getBookingById(id, auth));
    }

    /**
     * GET /api/bookings/ride/{rideId} — Get all bookings for a ride (driver/admin)
     */
    @GetMapping("/ride/{rideId}")
    @PreAuthorize("hasRole('CONDUCTEUR') or hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getBookingsByRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(bookingService.getBookingsByRide(rideId));
    }

    /**
     * PATCH /api/bookings/{id}/status?status=CONFIRMED — Update booking status (driver/admin)
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('CONDUCTEUR') or hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<BookingResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status,
            Authentication auth) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status, auth));
    }

    /**
     * DELETE /api/bookings/{id} — Passenger cancels their own booking
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PASSAGER') or hasRole('PASSENGER')")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication auth) {
        bookingService.cancelBooking(id, auth);
        return ResponseEntity.noContent().build();
    }
}

