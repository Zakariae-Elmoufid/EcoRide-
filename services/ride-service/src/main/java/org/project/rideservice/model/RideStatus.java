package org.project.rideservice.model;

public enum RideStatus {
    OPEN,           // Trajet ouvert (disponible pour réservations)
    FULL,           // Trajet complet (tous les sièges sont réservés)
    COMPLETED,      // Terminé (le trajet est fini)
    CANCELLED       // Annulé
}


