package org.project.rideservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseConnectionTest {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void testConnection() {
        try {
            log.info("====================================");
            log.info("🔍 TEST DE CONNEXION DATABASE");
            log.info("====================================");

            // Test de connexion simple
            String result = jdbcTemplate.queryForObject("SELECT version()", String.class);

            log.info("✅ CONNEXION RÉUSSIE !");
            log.info("📊 PostgreSQL Version: {}", result);

            // Vérifier si la table rides existe
            Integer tableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'rides'",
                Integer.class
            );

            if (tableCount != null && tableCount > 0) {
                log.info("✅ Table 'rides' existe");

                // Compter les rides
                Integer rideCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rides", Integer.class);
                log.info("📝 Nombre de rides dans la DB: {}", rideCount);
            } else {
                log.info("ℹ️  Table 'rides' n'existe pas encore (sera créée par Hibernate)");
            }

            log.info("====================================");
            log.info("✅ DATABASE RIDE SERVICE: OK !");
            log.info("====================================");

        } catch (Exception e) {
            log.error("====================================");
            log.error("❌ ERREUR DE CONNEXION DATABASE !");
            log.error("====================================");
            log.error("Message: {}", e.getMessage());
            log.error("Vérifiez que PostgreSQL est démarré sur le port 5433");
            log.error("Commande: docker-compose ps");
        }
    }
}

