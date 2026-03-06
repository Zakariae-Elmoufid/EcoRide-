package org.project.rideservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configuration de base de la sécurité
     * TODO: Configurer avec Yassin pour JWT et rôles DRIVER/PASSENGER
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les APIs REST
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics
                .requestMatchers(
                    "/api/rides/search/**",      // Recherche publique
                    "/api/rides/{id}",           // Détails publics
                    "/actuator/**",              // Actuator pour monitoring
                    "/error"                     // Gestion d'erreurs
                ).permitAll()

                // TODO avec Yassin: Restreindre POST /api/rides au rôle DRIVER uniquement
                // .requestMatchers(HttpMethod.POST, "/api/rides").hasRole("DRIVER")

                // TODO avec Yassin: Restreindre PUT /api/rides/{id}/complete au DRIVER propriétaire
                // .requestMatchers(HttpMethod.PUT, "/api/rides/{id}/complete").hasRole("DRIVER")

                // Pour le moment, tout est accessible (à sécuriser avec Yassin)
                .anyRequest().permitAll()
            );

        return http.build();
    }
}

