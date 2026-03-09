package org.example.bookingservice.service;

import org.example.bookingservice.dto.BookingCreatedEvent;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.entity.Booking;
import org.example.bookingservice.entity.BookingStatus;
import org.example.bookingservice.exception.BookingNotFoundException;
import org.example.bookingservice.feign.RideServiceClient;
import org.example.bookingservice.kafka.BookingEventProducer;
import org.example.bookingservice.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RefreshScope
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final BookingEventProducer eventProducer;
    private final RideServiceClient rideServiceClient;

    public BookingService(BookingRepository bookingRepository,
                          BookingEventProducer eventProducer,
                          RideServiceClient rideServiceClient) {
        this.bookingRepository = bookingRepository;
        this.eventProducer = eventProducer;
        this.rideServiceClient = rideServiceClient;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request, Authentication auth) {
        String passengerId = extractUserId(auth);
        String passengerEmail = extractEmail(auth);

        log.info("Creating booking for passengerId={}, rideId={}", passengerId, request.getRideId());

        Booking booking = Booking.builder()
                .rideId(request.getRideId())
                .passengerId(passengerId)
                .passengerEmail(passengerEmail)
                .seatsBooked(request.getSeatsBooked())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        // Publish Kafka event so ride-service reduces available seats
        eventProducer.publishBookingCreated(new BookingCreatedEvent(
                saved.getId(),
                saved.getRideId(),
                saved.getPassengerId(),
                saved.getSeatsBooked()
        ));

        log.info("Booking created with id={}", saved.getId());
        return toResponse(saved);
    }

    public List<BookingResponse> getMyBookings(Authentication auth) {
        String passengerId = extractUserId(auth);
        return bookingRepository.findByPassengerId(passengerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id, Authentication auth) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        String passengerId = extractUserId(auth);
        // skip ownership check for anonymous (local-dev) or admin
        if (!"anonymous".equals(passengerId)) {
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin && !booking.getPassengerId().equals(passengerId)) {
                throw new IllegalStateException("You are not allowed to view this booking");
            }
        }
        return toResponse(booking);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByRide(Long rideId) {
        return bookingRepository.findByRideId(rideId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse updateStatus(Long id, BookingStatus newStatus, Authentication auth) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        log.info("Updating booking id={} status from {} to {}", id, booking.getStatus(), newStatus);
        booking.setStatus(newStatus);
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public void cancelBooking(Long id, Authentication auth) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        String passengerId = extractUserId(auth);
        // skip ownership check for anonymous (local-dev)
        if (!"anonymous".equals(passengerId) && !booking.getPassengerId().equals(passengerId)) {
            throw new IllegalStateException("You are not allowed to cancel this booking");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        log.info("Booking id={} cancelled by passengerId={}", id, passengerId);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private String extractUserId(Authentication auth) {
        if (auth == null) return "anonymous";
        if (auth.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        return auth.getName();
    }

    private String extractEmail(Authentication auth) {
        if (auth == null) return null;
        if (auth.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("email");
        }
        return null;
    }

    private BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .rideId(b.getRideId())
                .passengerId(b.getPassengerId())
                .passengerEmail(b.getPassengerEmail())
                .status(b.getStatus())
                .seatsBooked(b.getSeatsBooked())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build();
    }
}

