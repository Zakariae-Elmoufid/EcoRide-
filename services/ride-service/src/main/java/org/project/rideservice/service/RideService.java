package org.project.rideservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.rideservice.client.UserServiceClient;
import org.project.rideservice.dto.DriverInfoDto;
import org.project.rideservice.dto.RideRequestDto;
import org.project.rideservice.dto.RideResponseDto;
import org.project.rideservice.exception.InsufficientSeatsException;
import org.project.rideservice.exception.InvalidOperationException;
import org.project.rideservice.exception.ResourceNotFoundException;
import org.project.rideservice.mapper.RideMapper;
import org.project.rideservice.model.Ride;
import org.project.rideservice.model.RideStatus;
import org.project.rideservice.repository.RideRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final UserServiceClient userServiceClient;

    /**
     * Créer un nouveau trajet (DRIVER uniquement)
     */

    public String getJwtFromRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            String header = attrs.getRequest().getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                return header;
            }
        }
        return null;
    }
    @Transactional
    public RideResponseDto createRide(RideRequestDto request) {
        log.info("Création d'un nouveau trajet par le driver ID: {}", request.getDriverId());
        String jwt = getJwtFromRequest(); // récupère le token depuis la requête entrante

        if (jwt == null) {
            throw new InvalidOperationException("JWT manquant dans la requête");
        }
        // Vérifier que le driver existe via Feign
        try {
            DriverInfoDto driverInfo = userServiceClient.getUserById(request.getDriverId(),jwt);
            log.info("Driver vérifié: {} {}", driverInfo.getFirstName(), driverInfo.getLastName());
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du driver: {}", e.getMessage());
            throw new InvalidOperationException("Le driver avec l'ID " + request.getDriverId() + " n'existe pas ou n'est pas disponible");
        }

        // Convertir DTO vers Entity
        Ride ride = rideMapper.toEntity(request);

        // Sauvegarder
        Ride savedRide = rideRepository.save(ride);
        log.info("Trajet créé avec succès, ID: {}", savedRide.getId());

        return rideMapper.toDto(savedRide);
    }

    /**
     * Rechercher des trajets par ville de départ, d'arrivée et date
     */
    @Transactional(readOnly = true)
    public List<RideResponseDto> searchRides(String from, String to, LocalDate date) {
        log.info("Recherche de trajets: from={}, to={}, date={}", from, to, date);

        List<Ride> rides;

        if (from != null && to != null && date != null) {
            // Recherche complète
            LocalDateTime dateTime = date.atStartOfDay();
            rides = rideRepository.searchRides(from, to, dateTime, RideStatus.OPEN);
        } else if (from != null && to != null) {
            // Recherche par villes uniquement
            LocalDateTime today = LocalDate.now().atStartOfDay();
            rides = rideRepository.searchRides(from, to, today, RideStatus.OPEN);
        } else if (from != null) {
            // Recherche par ville de départ uniquement
            rides = rideRepository.searchRidesByDeparture(from, RideStatus.OPEN);
        } else if (to != null) {
            // Recherche par ville d'arrivée uniquement
            rides = rideRepository.searchRidesByArrival(to, RideStatus.OPEN);
        } else {
            // Tous les trajets ouverts
            rides = rideRepository.findByStatusAndRemainingSeatsGreaterThan(RideStatus.OPEN, 0);
        }

        log.info("Nombre de trajets trouvés: {}", rides.size());

        return rides.stream()
                .map(rideMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtenir les détails d'un trajet par ID (avec info driver via Feign)
     */
    @Transactional(readOnly = true)
    public RideResponseDto getRideById(Long id) {
        log.info("Récupération du trajet ID: {}", id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet avec l'ID " + id + " introuvable"));

        RideResponseDto response = rideMapper.toDto(ride);
        String jwt = getJwtFromRequest(); // récupère le token depuis la requête entrante

        // Récupérer les infos du driver via Feign
        try {
            DriverInfoDto driverInfo = userServiceClient.getUserById(ride.getDriverId(),jwt);
            response.setDriverInfo(driverInfo);
        } catch (Exception e) {
            log.error("Impossible de récupérer les infos du driver: {}", e.getMessage());
            // On continue sans les infos du driver (fallback appliqué)
        }

        return response;
    }

    /**
     * Décrémenter les sièges disponibles (appelé par Booking Service)
     */
    @Transactional
    public RideResponseDto decrementSeats(Long id, Integer seatsToDecrement) {
        log.info("Décrémentation de {} siège(s) pour le trajet ID: {}", seatsToDecrement, id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet avec l'ID " + id + " introuvable"));

        // Vérifier le statut
        if (ride.getStatus() != RideStatus.OPEN) {
            throw new InvalidOperationException("Impossible de réserver: le trajet n'est pas ouvert (statut actuel: " + ride.getStatus() + ")");
        }

        // Vérifier les sièges disponibles
        if (ride.getRemainingSeats() < seatsToDecrement) {
            throw new InsufficientSeatsException(
                "Sièges insuffisants. Disponibles: " + ride.getRemainingSeats() + ", Demandés: " + seatsToDecrement
            );
        }

        // Décrémenter
        ride.setRemainingSeats(ride.getRemainingSeats() - seatsToDecrement);

        // Si plus de sièges, passer au statut FULL
        if (ride.getRemainingSeats() == 0) {
            ride.setStatus(RideStatus.FULL);
            log.info("Trajet ID: {} est maintenant FULL", id);
        }

        Ride updatedRide = rideRepository.save(ride);
        log.info("Sièges mis à jour. Sièges restants: {}", updatedRide.getRemainingSeats());

        return rideMapper.toDto(updatedRide);
    }

    /**
     * Terminer un trajet (passer au statut COMPLETED)
     */
    @Transactional
    public RideResponseDto completeRide(Long id) {
        log.info("Complétion du trajet ID: {}", id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet avec l'ID " + id + " introuvable"));

        // Vérifier que le trajet peut être complété
        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new InvalidOperationException("Le trajet est déjà terminé");
        }

        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidOperationException("Impossible de terminer un trajet annulé");
        }

        // Passer au statut COMPLETED
        ride.setStatus(RideStatus.COMPLETED);
        Ride completedRide = rideRepository.save(ride);

        log.info("Trajet ID: {} terminé avec succès", id);

        return rideMapper.toDto(completedRide);
    }

    /**
     * Obtenir tous les trajets d'un conducteur
     */
    @Transactional(readOnly = true)
    public List<RideResponseDto> getRidesByDriverId(Long driverId) {
        log.info("Récupération des trajets du driver ID: {}", driverId);

        List<Ride> rides = rideRepository.findByDriverIdOrderByDepartureTimeDesc(driverId);

        return rides.stream()
                .map(rideMapper::toDto)
                .collect(Collectors.toList());
    }
}


