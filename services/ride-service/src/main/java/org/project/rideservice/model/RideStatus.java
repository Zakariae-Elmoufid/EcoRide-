package org.project.rideservice.model;

public enum RideStatus {
    PENDING,        // En attente (trajet créé mais pas encore confirmé)
    CONFIRMED,      // Confirmé (le trajet est actif)
    IN_PROGRESS,    // En cours (le trajet a commencé)
    COMPLETED,      // Terminé (le trajet est fini)
    CANCELLED       // Annulé
}

